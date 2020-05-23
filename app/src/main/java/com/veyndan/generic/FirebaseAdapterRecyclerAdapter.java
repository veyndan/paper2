/*
 * Firebase UI Bindings Android Library
 *
 * Copyright © 2015 Firebase - All Rights Reserved
 * https://www.firebase.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binaryform must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY FIREBASE AS IS AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL FIREBASE BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.veyndan.generic;

import android.support.v7.widget.RecyclerView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.veyndan.generic.ui.HeaderRecyclerAdapter;

/**
 * This class is a generic way of backing an RecyclerView with a Firebase location.
 * It handles all of the child events at the given Firebase location. It marshals received data into the given
 * class type.
 * <p/>
 * To use this class in your app, subclass it passing in all required parameters and implement the
 * populateViewHolder method.
 * <p/>
 * <blockquote><pre>
 * {@code
 *     private static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
 *         TextView messageText;
 *         TextView nameText;
 * <p/>
 *         public ChatMessageViewHolder(View itemView) {
 *             super(itemView);
 *             nameText = (TextView)itemView.findViewById(android.R.id.text1);
 *             messageText = (TextView) itemView.findViewById(android.R.id.text2);
 *         }
 *     }
 * <p/>
 *     FirebaseAdapterRecyclerAdapter<ChatMessage, ChatMessageViewHolder> adapter;
 *     ref = new Firebase("https://<yourapp>.firebaseio.com");
 * <p/>
 *     RecyclerView recycler = (RecyclerView) findViewById(R.id.messages_recycler);
 *     recycler.setHasFixedSize(true);
 *     recycler.setLayoutManager(new LinearLayoutManager(this));
 * <p/>
 *     adapter = new FirebaseAdapterRecyclerAdapter<ChatMessage, ChatMessageViewHolder>(ChatMessage.class, android.R.layout.two_line_list_item, ChatMessageViewHolder.class, mRef) {
 *         public void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, ChatMessage chatMessage, int position) {
 *             chatMessageViewHolder.nameText.setText(chatMessage.getName());
 *             chatMessageViewHolder.messageText.setText(chatMessage.getMessage());
 *         }
 *     };
 *     recycler.setAdapter(mAdapter);
 * }
 * </pre></blockquote>
 *
 * @param <T>  The Java class that maps to the type of objects stored in the Firebase location.
 * @param <VH> The ViewHolder class that contains the Views in the layout that is shown for each object.
 */
public abstract class FirebaseAdapterRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends HeaderRecyclerAdapter<VH> {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(FirebaseAdapterRecyclerAdapter.class);

    Class<T> modelClass;
    FirebaseArray snapshots;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>
     */
    public FirebaseAdapterRecyclerAdapter(Class<T> modelClass, Query ref) {
        this.modelClass = modelClass;
        snapshots = new FirebaseArray(ref);

        snapshots.setOnChangedListener(new FirebaseArray.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                switch (type) {
                    case Added:
                        notifyItemInserted(index + HEADER_SIZE);
                        break;
                    case Changed:
                        notifyItemChanged(index);
                        break;
                    case Removed:
                        notifyItemRemoved(index);
                        break;
                    case Moved:
                        notifyItemMoved(oldIndex, index);
                        break;
                    default:
                        throw new IllegalStateException("Incomplete case statement");
                }
            }
        });
    }

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>
     */
    public FirebaseAdapterRecyclerAdapter(Class<T> modelClass, Firebase ref) {
        this(modelClass, (Query) ref);
    }

    public void cleanup() {
        snapshots.cleanup();
    }

    @Override
    protected int getContentItemCount() {
        return snapshots.getCount();
    }

    public T getItem(int position) {
        return parseSnapshot(snapshots.getItem(position));
    }

    /**
     * This method parses the DataSnapshot into the requested type. You can override it in subclasses
     * to do custom parsing.
     *
     * @param snapshot the DataSnapshot to extract the model from
     * @return the model extracted from the DataSnapshot
     */
    protected T parseSnapshot(DataSnapshot snapshot) {
        return snapshot.getValue(modelClass);
    }

    public Firebase getRef(int position) {
        return snapshots.getItem(position).getRef();
    }

    @Override
    public long getItemId(int position) {
        // http://stackoverflow.com/questions/5100071/whats-the-purpose-of-item-ids-in-android-listview-adapter
        return snapshots.getItem(position).getKey().hashCode();
    }
}