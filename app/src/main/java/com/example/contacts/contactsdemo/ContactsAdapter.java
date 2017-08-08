package com.example.contacts.contactsdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by muffin on 30/06/17.
 */
public class ContactsAdapter extends RecyclerView.Adapter {
    private List<ContactObj> contactList;
    Context ctx;

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private int lastAnimatedPosition = -1;

    public ContactsAdapter(Context ctx, ArrayList<ContactObj> cnList) {
        this.ctx = ctx;
        this.contactList = cnList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.ctx).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ContactObj cnObj = this.contactList.get(position);
        runEnterAnimation(viewHolder.itemView, position);
        ContactViewHolder holder = (ContactViewHolder) viewHolder;
        holder.contactName.setText(cnObj.displayName);
        holder.contactNumber.setText(cnObj.internationalNumber);
        if(cnObj.getPhotoURI() != null) {
            Glide.with(this.ctx).load(getContactPhotoUri(cnObj.getContactId())).placeholder(R.drawable.ic_user).crossFade().into(holder.ivUserAvatar);
        } else {
            Glide.with(this.ctx).load(getContactPhotoUri(cnObj.getContactId())).placeholder(R.drawable.ic_user).crossFade().into(holder.ivUserAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return this.contactList.size();
    }

    public void update(ArrayList<ContactObj> cnList) {
        this.contactList = cnList;
        notifyDataSetChanged();
    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivUserAvatar;
        TextView contactName, contactNumber;
        public ContactViewHolder(View view) {
            super(view);
            ivUserAvatar = (CircleImageView) itemView.findViewById(R.id.ivUserAvatar);
            contactName = (TextView) itemView.findViewById(R.id.contactNameTxtView);
            contactNumber = (TextView) itemView.findViewById(R.id.contactInternNationalNumber);
        }
    }

    public Uri getContactPhotoUri(long contactId) {
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        photoUri = Uri.withAppendedPath(photoUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        return photoUri;
    }
}
