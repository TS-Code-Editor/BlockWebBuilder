package com.block.web.builder.ui.activities;

import android.code.editor.common.utils.FileUtils;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.block.web.builder.R;
import com.block.web.builder.databinding.ActivityLicenseBinding;
import com.block.web.builder.ui.adapters.LicenseListAdapter;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

public class LicenseActivity extends BaseActivity {
  private ActivityLicenseBinding binding;
  private ArrayList<License> LicenseList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityLicenseBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    binding.toolbar.setTitle(R.string.app_name);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    binding.toolbar.setNavigationOnClickListener(
        view -> {
          onBackPressed();
        });

    String LicenseListFileText = FileUtils.readFileFromAssets(getAssets(), "LicenseList.json");
    LicenseList = new ArrayList<License>();

    try {
      JSONArray arr = new JSONArray(LicenseListFileText);
      for (int pos = 0; pos < arr.length(); ++pos) {
        if (arr.getJSONObject(pos).has("Name") && arr.getJSONObject(pos).has("Path")) {
          License License = new License();
          License.setLicenseName(arr.getJSONObject(pos).getString("Name"));
          License.setLicensePath(arr.getJSONObject(pos).getString("Path"));
          LicenseList.add(License);
        }
      }
    } catch (JSONException e) {
    }
    binding.list.setAdapter(new LicenseListAdapter(LicenseList, this));
    binding.list.setLayoutManager(new LinearLayoutManager(this));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  public class License {
    private String LicenseName;
    private String LicensePath;

    public String getLicenseName() {
      return this.LicenseName;
    }

    public void setLicenseName(String LicenseName) {
      this.LicenseName = LicenseName;
    }

    public String getLicensePath() {
      return this.LicensePath;
    }

    public void setLicensePath(String LicensePath) {
      this.LicensePath = LicensePath;
    }
  }
}