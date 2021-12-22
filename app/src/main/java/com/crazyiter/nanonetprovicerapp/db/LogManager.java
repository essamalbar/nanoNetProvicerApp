package com.crazyiter.nanonetprovicerapp.db;

import com.crazyiter.nanonetprovicerapp.R;
import com.crazyiter.nanonetprovicerapp.Statics;
import com.crazyiter.nanonetprovicerapp.VolleyManager;

import org.json.JSONArray;
import org.json.JSONException;

public class LogManager {

    private static final int RENEW = 1;
    private static final int PAY = 2;
    private static final int AMOUNT = 3;

    public static LogModel renew(Provider provider, boolean isGet, int cost, Customer customer) {
        String body;
        if (isGet) {
            body = "إضافة اشتراك بقيمة: " + Statics.formatNumber(cost * 1000) + " (واصل)";
        } else {
            body = "إضافة اشتراك بقيمة: " + Statics.formatNumber(cost * 1000);
        }

        LogModel logModel = new LogModel("", Statics.LocalDate.getCurrentDate(), body, customer.getId());
        checkAlertSettings(provider, RENEW, logModel);
        return logModel;
    }

    public static LogModel addAmount(Provider provider, int amount, Customer customer) {
        if (amount == 0) {
            return null;
        }
        String body = "إضافة دين بقيمة: " + Statics.formatNumber(amount * 1000);
        LogModel logModel = new LogModel("", Statics.LocalDate.getCurrentDate(), body, customer.getId());
        checkAlertSettings(provider, AMOUNT, logModel);
        return logModel;
    }

    public static LogModel payAmount(Provider provider, int cash, Customer customer) {
        String body = "تسديد مبلغ: " + Statics.formatNumber(cash * 1000);
        LogModel logModel = new LogModel("", Statics.LocalDate.getCurrentDate(), body, customer.getId());
        checkAlertSettings(provider, PAY, logModel);
        return logModel;
    }

    private static void checkAlertSettings(Provider provider, int type, LogModel logModel) {
        boolean b = false;
        switch (type) {
            case RENEW:
                b = provider.isRenewAlert();
                break;

            case PAY:
                b = provider.isPayAlert();
                break;

            case AMOUNT:
                b = provider.isAmountAlert();
                break;
        }
        if (b) {
            try {
                AlertModel alertModel = new AlertModel("", provider.getId(), logModel.getBody(), logModel.getDateTime(), false, R.drawable.ic_launcher_foreground, R.color.colorPrimary);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(logModel.getCustomerId());
                alertModel.setCustomerIds(jsonArray);
                VolleyManager.sendNotification(alertModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
