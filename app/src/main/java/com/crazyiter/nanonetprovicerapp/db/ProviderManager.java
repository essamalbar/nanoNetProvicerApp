package com.crazyiter.nanonetprovicerapp.db;

import com.crazyiter.nanonetprovicerapp.R;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class ProviderManager {

    private final static String col = "providers";

    public static void login(String email, String password, LoginProviderListener loginProviderListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots == null || queryDocumentSnapshots.isEmpty()) {
                        loginProviderListener.onLogin(null);
                        return;
                    }

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        boolean u = email.equals(doc.getString("email"));
                        boolean p = password.equals(doc.getString("password"));
                        if (u && p) {
                            int order;
                            try {
                                order = Integer.parseInt(String.valueOf(doc.get("orderBy")));
                            } catch (Exception e) {
                                order = R.id.nameRB;
                            }

                            try {
                                Provider provider = new Provider(
                                        doc.getId(),
                                        doc.getString("name"),
                                        doc.getString("email"),
                                        doc.getString("password"),
                                        doc.getString("startDate"),
                                        Integer.parseInt(String.valueOf(doc.get("type"))),
                                        Integer.parseInt(String.valueOf(doc.get("count"))),
                                        order,
                                        doc.getString("adminId")
                                );

                                provider.setActive(doc.getBoolean("isActive"));
                                provider.setRenewAlert(doc.getBoolean("renewAlert"));
                                provider.setPayAlert(doc.getBoolean("payAlert"));
                                provider.setAmountAlert(doc.getBoolean("amountAlert"));
                                provider.setTicketAlert(doc.getBoolean("ticketAlert"));

                                loginProviderListener.onLogin(provider);

                            } catch (Exception e) {
                                loginProviderListener.onLogin(null);
                            }
                        }
                    }

                    loginProviderListener.onLogin(null);
                });
    }

    public static void update(Provider provider) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(provider.getId())
                .update(provider.getMap());
    }

    public static void update(Provider provider, Map<String, Object> map) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(provider.getId())
                .update(map);
    }

    public static void logout(Provider provider) {
        Map<String, Object> map = new HashMap<>();
        map.put("fcm", "");
        map.put("isOnline", false);
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(provider.getId())
                .update(map);
    }

    public static void checkProvider(Provider p, LoginProviderListener loginProviderListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(p.getId())
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc != null) {
                        int order;
                        try {
                            order = Integer.parseInt(String.valueOf(doc.get("orderBy")));
                        } catch (Exception e) {
                            order = R.id.nameRB;
                        }

                        int type;
                        try {
                            type = Integer.parseInt(String.valueOf(doc.get("type")));
                        } catch (Exception e) {
                            type = 0;
                        }

                        int count;
                        try {
                            count = Integer.parseInt(String.valueOf(doc.get("count")));
                        } catch (Exception e) {
                            count = 0;
                        }

                        try {
                            Provider provider = new Provider(
                                    doc.getId(),
                                    doc.getString("name"),
                                    doc.getString("email"),
                                    doc.getString("password"),
                                    doc.getString("startDate"),
                                    type,
                                    count,
                                    order,
                                    doc.getString("adminId")
                            );

                            provider.setActive(doc.getBoolean("isActive"));
                            provider.setRenewAlert(doc.getBoolean("renewAlert"));
                            provider.setPayAlert(doc.getBoolean("payAlert"));
                            provider.setAmountAlert(doc.getBoolean("amountAlert"));
                            provider.setTicketAlert(doc.getBoolean("ticketAlert"));

                            loginProviderListener.onLogin(provider);

                        } catch (Exception e) {
                            loginProviderListener.onLogin(null);
                        }
                    } else {
                        loginProviderListener.onLogin(null);
                    }
                });
    }

    public interface LoginProviderListener {
        void onLogin(Provider provider);
    }

}
