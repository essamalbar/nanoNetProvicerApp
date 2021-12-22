package com.crazyiter.nanonetprovicerapp.db;

import com.crazyiter.nanonetprovicerapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class CustomerManager {

    private final static String col = "customers";
    public static final ArrayList<Customer> customers = new ArrayList<>();

    public static void getCustomers(Provider provider, GetCustomersListener getCustomersListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .whereEqualTo("providerId", provider.getId())
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        customers.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Customer c = new Customer(
                                    doc.getId(),
                                    doc.getString("name"),
                                    doc.getString("user"),
                                    doc.getString("mobile"),
                                    doc.getString("startDate"),
                                    Integer.parseInt(String.valueOf(doc.get("cost"))),
                                    provider.getId(),
                                    Integer.parseInt(String.valueOf(doc.get("amount"))),
                                    doc.getString("nanoUrl"),
                                    doc.getString("nanoUsername"),
                                    doc.getString("nanoPassword"),
                                    doc.getString("note"),
                                    doc.getString("email"),
                                    doc.getString("password"),
                                    Integer.parseInt(String.valueOf(doc.get("days"))),
                                    doc.getString("fcm")
                            );

                            try {
                                c.setLogs(new JSONArray(doc.getString("logs")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                c.setActive(doc.getBoolean("isActive"));
                            } catch (Exception e) {
                                c.setActive(true);
                            }
                            customers.add(c);
                        }
                    }
                    getCustomersListener.onLoad(customers);
                });
    }

    public static void addCustomer(Customer customer) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .add(customer.getMap());
    }

    public static void getCustomer(String customer, GetSingleCustomerListener getSingleCustomerListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(customer)
                .addSnapshotListener((value, error) -> {
                    if (value == null) {
                        getSingleCustomerListener.onLoad(null);
                    } else {

                        try {
                            Customer c = new Customer(
                                    value.getId(),
                                    value.getString("name"),
                                    value.getString("user"),
                                    value.getString("mobile"),
                                    value.getString("startDate"),
                                    Integer.parseInt(String.valueOf(value.get("cost"))),
                                    value.getString("providerId"),
                                    Integer.parseInt(String.valueOf(value.get("amount"))),
                                    value.getString("nanoUrl"),
                                    value.getString("nanoUsername"),
                                    value.getString("nanoPassword"),
                                    value.getString("note"),
                                    value.getString("email"),
                                    value.getString("password"),
                                    Integer.parseInt(String.valueOf(value.get("days"))),
                                    value.getString("fcm")
                            );
                            c.setLogs(new JSONArray(value.getString("logs")));

                            try {
                                c.setActive(value.getBoolean("isActive"));
                            } catch (Exception e) {
                                c.setActive(true);
                            }
                            getSingleCustomerListener.onLoad(c);
                        } catch (Exception e) {
//                            getSingleCustomerListener.onLoad(null);
                        }

                    }
                });
    }

    public static void findCustomer(String customer, GetSingleCustomerListener getSingleCustomerListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(customer)
                .get()
                .addOnSuccessListener((value) -> {
                    if (value == null) {
                        getSingleCustomerListener.onLoad(null);
                    } else {

                        try {
                            Customer c = new Customer(
                                    value.getId(),
                                    value.getString("name"),
                                    value.getString("user"),
                                    value.getString("mobile"),
                                    value.getString("startDate"),
                                    Integer.parseInt(String.valueOf(value.get("cost"))),
                                    value.getString("providerId"),
                                    Integer.parseInt(String.valueOf(value.get("amount"))),
                                    value.getString("nanoUrl"),
                                    value.getString("nanoUsername"),
                                    value.getString("nanoPassword"),
                                    value.getString("note"),
                                    value.getString("email"),
                                    value.getString("password"),
                                    Integer.parseInt(String.valueOf(value.get("days"))),
                                    value.getString("fcm")
                            );
                            c.setLogs(new JSONArray(value.getString("logs")));

                            try {
                                c.setActive(value.getBoolean("isActive"));
                            } catch (Exception e) {
                                c.setActive(true);
                            }
                            getSingleCustomerListener.onLoad(c);
                        } catch (Exception e) {
//                            getSingleCustomerListener.onLoad(null);
                        }

                    }
                });
    }

    public static int existCount(Customer customer) {
        int s = 0;
        for (int i = 0; i < customers.size(); i++) {
            if (customer.getId().equals(customers.get(i).getId())) {
                continue;
            }

            if (customer.isEquals(customers.get(i))) {
                s++;
            }
        }
        return s;
    }

    public static int[] getStatuesCount() {
        int[] difs = new int[]{0, 0, 0};
        for (int i = 0; i < customers.size(); i++) {
            int dif = (int) Math.ceil(customers.get(i).getDiffDays());

            if (dif > 3) {
                difs[0]++;
                continue;
            }

            if (dif > 0) {
                difs[1]++;
                continue;
            }

            difs[2]++;
        }
        return difs;
    }

    public static void deleteCustomer(Customer customer) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(customer.getId())
                .delete();
    }

    public static void updateCustomer(Customer customer) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(customer.getId())
                .update(customer.getMap());
    }

    public static void updateCustomer(Customer customer, Map<String, Object> map) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(customer.getId())
                .update(map);
    }

    public static void sort(Provider provider, ArrayList<Customer> temp) {
        Collections.sort(temp, (o1, o2) -> {
            int i = provider.getOrderBy();
            if (i == R.id.nameRB) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }

            if (i == R.id.daysRB) {
                if (o1.getDiffDays() >= o2.getDiffDays()) return 1;
                else return -1;
            }

            if (o1.getAmount() >= o2.getAmount()) return 1;
            else return -1;
        });
    }

    public interface GetSingleCustomerListener {
        void onLoad(Customer customers);
    }

    public interface GetCustomersListener {
        void onLoad(ArrayList<Customer> customers);
    }

}
