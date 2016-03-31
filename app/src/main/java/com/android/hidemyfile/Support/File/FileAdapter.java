package com.android.hidemyfile.Support.File;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.hidemyfile.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.BaseViewHolder> {

    private Context context;
    private ArrayList<FileModel> dataSet;
    private RecyclerViewCallbacks recyclerViewCallbacks;

    public FileAdapter(Context context, ArrayList<FileModel> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rootView;
        ImageView fileImage;
        TextView fileName;
        TextView filePath;

        public BaseViewHolder(View itemView) {
            super(itemView);

            this.rootView = (RelativeLayout) itemView.findViewById(R.id.root_view);
            this.fileImage = (ImageView) itemView.findViewById(R.id.image_icon);
            this.fileName = (TextView) itemView.findViewById(R.id.text_name);
            this.filePath = (TextView) itemView.findViewById(R.id.text_path);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_encrypted, parent, false);

        return new BaseViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        FileModel fileModel = dataSet.get(position);

        RelativeLayout rootView = holder.rootView;
        ImageView fileImage = holder.fileImage;
        TextView fileName = holder.fileName;
        TextView filePath = holder.filePath;

        Glide
                .with(context)
                .load(FileUtils.getIconFromType(
                        FileUtils.getEncryptedFileType(dataSet.get(position).getFilePath())
                ))
                .crossFade()
                .into(fileImage);

        fileName.setText(fileModel.getFileName());
        filePath.setText(fileModel.getFilePath());
        fileName.setSelected(true);
        filePath.setSelected(true);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewCallbacks != null) {
                    recyclerViewCallbacks.itemClicked(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setRecyclerViewCallbacks(RecyclerViewCallbacks recyclerViewCallbacks) {
        this.recyclerViewCallbacks = recyclerViewCallbacks;
    }

    public interface RecyclerViewCallbacks {
        void itemClicked(int position);
    }
}
