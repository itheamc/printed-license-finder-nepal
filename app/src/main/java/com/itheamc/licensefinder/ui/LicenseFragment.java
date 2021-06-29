package com.itheamc.licensefinder.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.itheamc.licensefinder.R;
import com.itheamc.licensefinder.api.Urls;
import com.itheamc.licensefinder.databinding.FragmentLicenseBinding;
import com.itheamc.licensefinder.models.License;
import com.itheamc.licensefinder.models.User;
import com.itheamc.licensefinder.utils.FormatDate;
import com.itheamc.licensefinder.utils.StorageUtility;
import com.itheamc.licensefinder.viewmodel.SharedViewModel;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LicenseFragment extends Fragment {
    private static final String TAG = "LicenseFragment";
    private FragmentLicenseBinding licenseBinding;
    private ExecutorService executorService;
    private Handler handler;
    private NavController navController;
    private SharedViewModel viewModel;

    public LicenseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        licenseBinding = FragmentLicenseBinding.inflate(inflater, container, false);
        return licenseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        //ViewModel initialization
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        executorService = Executors.newFixedThreadPool(4);
        handler = HandlerCompat.createAsync(Looper.getMainLooper());

        OkHttpClient client = new OkHttpClient();

        licenseBinding.progressBar.setVisibility(View.VISIBLE);

        String dob_ad = StorageUtility.getBirthDate(requireActivity());
        if (dob_ad != null) {
            String lcNo = StorageUtility.getLicenseNumber(requireActivity());
            if (lcNo != null) {
                fetchBasicInfo(client,dob_ad + "T00:00:00+05:45", lcNo);
            } else {
                fetchBasicInfo(client,dob_ad + "T00:00:00+05:45", viewModel.getLicense().get_license_no());
            }
        } else {
            notifyFailure("Unable to find your dob");
        }
    }

    // Function to get basic data from the server
    private void fetchBasicInfo(OkHttpClient client, String dob, String licNo) {
        executorService.execute(() -> {
            RequestBody requestBody = new FormBody.Builder()
                    .add("action:drivingLicenseSearch_getApplicantsByLicenseNo", "CHECK STATUS")
                    .add("appStatus", "lic")
                    .add("dob", dob)
                    .add("licenseNo", licNo)
                    .build();

            Request request = new Request.Builder().url(Urls.BASIC_INFO_REQUEST_URL).post(requestBody).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    notifyFailure(e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    parseBasicInfoResponse(response.body().string(), client);
                }
            });
        });
    }


    // Function to parse html response of first network request
    private void parseBasicInfoResponse(String res, OkHttpClient client) {
        List<String> info = new ArrayList<>();
        Document document = Jsoup.parse(res);
        Element body = document.body();
        try {
            Elements containers = body.getElementsByClass("datagrid");
            for (Element container: containers) {
                Element table = container.getElementsByTag("table").get(0);
                Element tBody = table.getElementsByTag("tbody").get(0);
                Elements tRows = tBody.getElementsByTag("tr");
                for (Element tr: tRows) {
                    Elements tData = tr.getElementsByTag("td");
                    for (Element td: tData) {
                        String val = td.text().replace(".", "");
                        info.add(val);
                    }
                }
            }

            Log.d(TAG, "parseBasicInfoResponse: " + info.toString());
            fetchUserData(client, info);

        } catch (Exception e) {
            e.printStackTrace();
            notifyFailure(e.getMessage());
        }
    }


    // Function to get details data of the user from the server
    private void fetchUserData(OkHttpClient client, List<String> info) {
        RequestBody requestBody = new FormBody.Builder()
                .add("appId", info.get(3))
                .add("citizenIdFromForm", info.get(4))
                .add("dob", FormatDate.format1(info.get(2)))
                .add("newLicenseNo", info.get(5))
                .add("select", "SELECT")
                .build();

        Request request = new Request.Builder().url(Urls.DETAIL_INFO_REQUEST_URL).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                notifyFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                parseUserData(response.body().string());
            }
        });
    }

    // Function to parse response got from fetchUserData() function
    private void parseUserData(String response) {
        List<User> userInfo = new ArrayList<>();
        List<String> lists = new ArrayList<>();
        Document document = Jsoup.parse(response);
        Element body = document.body();
        try {
//            Elements tables = body.getElementsByTag("table");
            Elements containers = body.getElementsByClass("datagrid");
            for (Element container: containers) {
                Element table = container.getElementsByTag("table").get(0);
                Element tbody = table.getElementsByTag("tbody").get(0);
                Elements tRows = tbody.getElementsByTag("tr");
                for (Element tr: tRows) {
                    Elements tDatas = tr.getElementsByTag("td");
                    for (Element td: tDatas) {
                        if (td.getElementsByTag("img") != null && td.getElementsByTag("img").size() > 0) {
//                            System.out.println("Image Url");
//                            System.out.println(td.getElementsByTag("img").get(0).attr("src"));
                            lists.add("imageurl");
                            lists.add(td.getElementsByTag("img").get(0).attr("src"));
                        } else {
                            String val = td.text().replace(".", "");
//                            System.out.println(val);
                            lists.add(val);
                        }
                    }
                }
            }

            for (int i = 0; i < lists.size(); i++) {
                if (i % 2 == 1) {
                    User user = new User(
                            lists.get(i-1),
                            lists.get(i)
                    );
                    userInfo.add(user);
                }
            }

            Log.d(TAG, "parseUserData: " + userInfo.toString());
            notifySuccess(userInfo);

        } catch (Exception e) {
            e.printStackTrace();
            notifyFailure(e.getMessage());
        }
    }




    // Function to notify success
    private void notifySuccess(List<User> userData) {
        handler.post(() -> {
            License license = new License();
            for (int i = 0; i < userData.size(); i++) {
                if (i == 0) license.set_citizenship_no(userData.get(i).getValue());
                if (i == 1) license.set_image(Urls.SUBDOMAIN_BASE_URL + userData.get(i).getValue());
                else if(i == 3) license.set_license_no(userData.get(i).getValue());
                else if (i == 4) license.set_name(userData.get(i).getValue());
                else if (i == 7) license.set_dob_ad(FormatDate.format2(userData.get(i).getValue()));
                else if (i == 8) license.set_blood_group(userData.get(i).getValue());
                else if (i == 10) license.set_ward_number(userData.get(i).getValue());
                else if (i == 11) license.set_tole(userData.get(i).getValue());
                else if (i == 13) license.set_zone(userData.get(i).getValue());
                else if (i == 14) license.set_district(userData.get(i).getValue());
                else if (i == 15) license.set_municipality(userData.get(i).getValue());
                else if (i == 22) license.set_issue_date(FormatDate.format2(userData.get(i).getValue().substring(0, 11)));
                else if (i == 23)  {
                    license.set_expiry_date(FormatDate.format2(userData.get(i).getKey().substring(0, 11)));
                    license.set_category(userData.get(i).getValue().split("-")[0]);
                }
                else if (i == 24) license.set_issuer(userData.get(i).getKey());
            }

            licenseBinding.progressBar.setVisibility(View.GONE);
            licenseBinding.constraintLayout.setVisibility(View.VISIBLE);
            licenseBinding.userInfoLabel.setVisibility(View.VISIBLE);
            licenseBinding.setLicense(license);
            StorageUtility.setLicenseNumber(requireActivity(), license.get_license_no());
            Log.d(TAG, "run: " + license.toString());
        });

    }

    // Function to notify failure
    private void notifyFailure(String message) {
        handler.post(() -> {
            licenseBinding.progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Sorry!!, we are unable to create demo license for you.", Toast.LENGTH_LONG).show();
            Log.d(TAG, "run: " + message);
            try {
                Thread.sleep(1000);
                navController.navigate(R.id.action_licenseFragment_to_detailsFragment);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

    }

}