package com.android.ql.lf.electronicbusiness.ui.adapters.im;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.data.MessageInfo;
import com.android.ql.lf.electronicbusiness.ui.views.BubbleImageView;
import com.android.ql.lf.electronicbusiness.utils.im.Constants;
import com.android.ql.lf.electronicbusiness.utils.im.Utils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author Administrator
 */
public class ChatAdapter extends BaseMultiItemQuickAdapter<MessageInfo, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatAdapter(List<MessageInfo> data) {
        super(data);
        addItemType(Constants.CHAT_ITEM_TYPE_LEFT, R.layout.im_item_chat_accept);
        addItemType(Constants.CHAT_ITEM_TYPE_RIGHT, R.layout.im_tem_chat_send);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageInfo item) {
        switch (item.getItemType()) {
            case Constants.CHAT_ITEM_TYPE_LEFT:
                TextView tvDate = helper.getView(R.id.chat_item_date);
                ImageView chatItemHeader = helper.getView(R.id.chat_item_header);
                TextView chatItemContentText = helper.getView(R.id.chat_item_content_text);
                BubbleImageView chatItemContentImage = helper.getView(R.id.chat_item_content_image);
                ImageView chatItemVoice = helper.getView(R.id.chat_item_voice);
                LinearLayout chatItemLayoutContent = helper.getView(R.id.chat_item_layout_content);
                TextView chatItemVoiceTime = helper.getView(R.id.chat_item_voice_time);
                tvDate.setText(item.getTime() != null ? item.getTime() : "");
//                Glide.with(tvDate.getContext()).load(item.getHeader()).into(chatItemHeader);
                if (item.getContent() != null) {
                    chatItemContentText.setText(item.getContent());
                    chatItemVoice.setVisibility(View.GONE);
                    chatItemContentText.setVisibility(View.VISIBLE);
                    chatItemLayoutContent.setVisibility(View.VISIBLE);
                    chatItemVoiceTime.setVisibility(View.GONE);
                    chatItemContentImage.setVisibility(View.GONE);
                } else if (item.getImageUrl() != null) {
                    chatItemVoice.setVisibility(View.GONE);
                    chatItemLayoutContent.setVisibility(View.GONE);
                    chatItemVoiceTime.setVisibility(View.GONE);
                    chatItemContentText.setVisibility(View.GONE);
                    chatItemContentImage.setVisibility(View.VISIBLE);
                    Glide.with(tvDate.getContext()).load(item.getImageUrl()).into(chatItemContentImage);
                } else if (item.getFilepath() != null) {
                    helper.addOnClickListener(R.id.chat_item_layout_content);
                    chatItemVoice.setVisibility(View.VISIBLE);
                    chatItemLayoutContent.setVisibility(View.VISIBLE);
                    chatItemContentText.setVisibility(View.GONE);
                    chatItemVoiceTime.setVisibility(View.VISIBLE);
                    chatItemContentImage.setVisibility(View.GONE);
                    chatItemVoiceTime.setText(Utils.formatTime(item.getVoiceTime()));
                }
                break;
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                TextView tvDateSend = helper.getView(R.id.chat_item_date_send);
                ImageView chatItemHeaderSend = helper.getView(R.id.chat_item_header_send);
                TextView chatItemContentTextSend = helper.getView(R.id.chat_item_content_text_send);
                BubbleImageView chatItemContentImageSend = helper.getView(R.id.chat_item_content_image_send);
                ImageView chatItemVoiceSend = helper.getView(R.id.chat_item_voice_send);
                LinearLayout chatItemLayoutContentSend = helper.getView(R.id.chat_item_layout_content_send);
                TextView chatItemVoiceTimeSend = helper.getView(R.id.chat_item_voice_time_send);
                ProgressBar chatItemProgress = helper.getView(R.id.chat_item_progress_send);
                ImageView chatItemFail = helper.getView(R.id.chat_item_fail_send);
                tvDateSend.setText(item.getTime() != null ? item.getTime() : "");
//                Glide.with(tvDateSend.getContext()).load(item.getHeader()).into(chatItemHeaderSend);
                if (item.getContent() != null) {
                    chatItemContentTextSend.setText(item.getContent());
                    chatItemVoiceSend.setVisibility(View.GONE);
                    chatItemContentTextSend.setVisibility(View.VISIBLE);
                    chatItemLayoutContentSend.setVisibility(View.VISIBLE);
                    chatItemVoiceTimeSend.setVisibility(View.GONE);
                    chatItemContentImageSend.setVisibility(View.GONE);
                } else if (item.getImageUrl() != null) {
                    chatItemVoiceSend.setVisibility(View.GONE);
                    chatItemLayoutContentSend.setVisibility(View.GONE);
                    chatItemVoiceTimeSend.setVisibility(View.GONE);
                    chatItemContentTextSend.setVisibility(View.GONE);
                    chatItemContentImageSend.setVisibility(View.VISIBLE);
                    Glide.with(tvDateSend.getContext()).load(item.getImageUrl()).into(chatItemContentImageSend);
                } else if (item.getFilepath() != null) {
                    helper.addOnClickListener(R.id.chat_item_layout_content_send);
                    chatItemVoiceSend.setVisibility(View.VISIBLE);
                    chatItemLayoutContentSend.setVisibility(View.VISIBLE);
                    chatItemContentTextSend.setVisibility(View.GONE);
                    chatItemVoiceTimeSend.setVisibility(View.VISIBLE);
                    chatItemContentImageSend.setVisibility(View.GONE);
                    chatItemVoiceTimeSend.setText(Utils.formatTime(item.getVoiceTime()));
                }
                switch (item.getSendState()) {
                    case Constants.CHAT_ITEM_SENDING:
                        chatItemProgress.setVisibility(View.VISIBLE);
                        chatItemFail.setVisibility(View.GONE);
                        break;
                    case Constants.CHAT_ITEM_SEND_ERROR:
                        chatItemProgress.setVisibility(View.GONE);
                        chatItemFail.setVisibility(View.VISIBLE);
                        break;
                    case Constants.CHAT_ITEM_SEND_SUCCESS:
                        chatItemProgress.setVisibility(View.GONE);
                        chatItemFail.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
