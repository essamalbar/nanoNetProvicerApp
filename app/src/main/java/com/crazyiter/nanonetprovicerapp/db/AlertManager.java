package com.crazyiter.nanonetprovicerapp.db;

import com.crazyiter.nanonetprovicerapp.Statics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class AlertManager {

    private final static String col = "alerts";

    public static void getAlerts(Provider provider, LoadAlerts loadAlerts) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .whereEqualTo("providerId", provider.getId())
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        ArrayList<AlertModel> alertModels = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            AlertModel alertModel = new AlertModel(
                                    doc.getId(),
                                    doc.getString("providerId"),
                                    doc.getString("message"),
                                    doc.getString("dateTime"),
                                    false,
                                    Integer.parseInt(String.valueOf(doc.get("typeRes"))),
                                    Integer.parseInt(String.valueOf(doc.get("typeColor")))
                            );
                            try {
                                alertModel.setCustomerIds(new JSONArray(doc.getString("customerIds")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            alertModels.add(alertModel);
                        }

                        Collections.sort(alertModels, (o1, o2) -> {
                            long t1 = Statics.LocalDate.getTime(o1.getDateTime());
                            long t2 = Statics.LocalDate.getTime(o2.getDateTime());
                            if (t1 > t2) return -1;
                            else if (t1 < t2) return 1;
                            return 0;
                        });

                        loadAlerts.onLoad(alertModels);
                    } else {
                        loadAlerts.onLoad(null);
                    }
                });
    }

    public static void addAlert(AlertModel alertModel) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .add(alertModel.getMap());
    }

    public static void deleteAlert(AlertModel alertModel) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(alertModel.getId())
                .delete();
    }

    public interface LoadAlerts {
        void onLoad(ArrayList<AlertModel> alertModels);
    }

}
