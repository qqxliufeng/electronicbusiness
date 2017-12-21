package com.android.ql.lf.electronicbusiness.utils;

import android.content.Context;
import android.view.View;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by lf on 2017/12/21 0021.
 *
 * @author lf on 2017/12/21 0021
 */

public class BadgeViewFactory {

    public static QBadgeView createBadge(Context context, View targetView, int num, float gravityX, float gravityY, boolean isDpValue) {
        QBadgeView badgeView = new QBadgeView(context);
        Badge badge = badgeView.bindTarget(targetView);
        badge.setBadgeNumber(num);
        badge.setGravityOffset(gravityX, gravityY, isDpValue);
        badge.setShowShadow(false);
        return badgeView;
    }

}
