/*
 * This file is part of BlockWeb Builder [https://github.com/TS-Code-Editor/BlockWebBuilder].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.block.web.builder.ui.activities;

import static com.block.web.builder.utils.Environments.BLOCKS;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.block.web.builder.R;
import com.block.web.builder.databinding.ActivityBlocksHolderManagerBinding;
import com.block.web.builder.listeners.BlocksHolderListener;
import com.block.web.builder.core.BlocksHolder;
import com.block.web.builder.ui.adapters.BlocksHolderAdapter;
import com.block.web.builder.ui.dialogs.blocksholder.CreateBlocksHolderDialog;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BlocksHolderManagerActivity extends BaseActivity {

  private ActivityBlocksHolderManagerBinding binding;
  private ArrayList<BlocksHolder> blocksHolderList;

  @Override
  protected void onCreate(Bundle arg0) {
    super.onCreate(arg0);

    // Inflate and get instance of binding.
    binding = ActivityBlocksHolderManagerBinding.inflate(getLayoutInflater());

    // set content view to binding's root.
    setContentView(binding.getRoot());

    // Setup toolbar.
    binding.toolbar.setTitle(R.string.app_name);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    binding.toolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View arg0) {
            onBackPressed();
          }
        });

    // Initialize blocksHolderList to avoid null errors
    blocksHolderList = new ArrayList<BlocksHolder>();

    /*
     * Ask for storage permission if not granted.
     * Load blocks holder list if storage permission is granted.
     */
    if (MainActivity.isStoagePermissionGranted(this)) {
      loadBlocksHolderList();
      binding.fab.setOnClickListener(
          (view) -> {
            CreateBlocksHolderDialog createBlocksHolder =
                new CreateBlocksHolderDialog(
                    this,
                    blocksHolderList,
                    new BlocksHolderListener() {
                      @Override
                      public void onBlockHolderCreate(BlocksHolder holder) {
                        binding.list.setAdapter(
                            new BlocksHolderAdapter(
                                blocksHolderList, BlocksHolderManagerActivity.this));
                        binding.list.setLayoutManager(
                            new LinearLayoutManager(BlocksHolderManagerActivity.this));
                        showSection(3);
                      }

                      @Override
                      public void onBlocksHolderFailedToCreate(String error) {
                        Toast.makeText(BlocksHolderManagerActivity.this, error, Toast.LENGTH_SHORT)
                            .show();
                      }
                    });
            createBlocksHolder.create().show();
          });
    } else {
      showSection(2);
      MainActivity.showStoragePermissionDialog(this);
    }
  }

  private void loadBlocksHolderList() {
    showSection(1);
    Executor executor = Executors.newSingleThreadExecutor();
    executor.execute(
        () -> {
          if (BLOCKS.exists()) {
            try {
              FileInputStream fis = new FileInputStream(BLOCKS);
              ObjectInputStream ois = new ObjectInputStream(fis);
              Object obj = ois.readObject();
              if (obj instanceof ArrayList) {
                blocksHolderList = (ArrayList<BlocksHolder>) obj;
                runOnUiThread(
                    () -> {
                      binding.list.setAdapter(
                          new BlocksHolderAdapter(
                              blocksHolderList, BlocksHolderManagerActivity.this));
                      binding.list.setLayoutManager(
                          new LinearLayoutManager(BlocksHolderManagerActivity.this));
                      showSection(3);
                    });
              } else {
                runOnUiThread(
                    () -> {
                      showSection(2);
                      binding.tvInfo.setText(
                          getString(R.string.an_error_occured_while_parsing_blocks_holder_list));
                    });
              }
              fis.close();
              ois.close();
            } catch (Exception e) {
              runOnUiThread(
                  () -> {
                    showSection(2);
                    binding.tvInfo.setText(e.getMessage());
                  });
            }
          } else {
            runOnUiThread(
                () -> {
                  showSection(2);
                  binding.tvInfo.setText(getString(R.string.no_blocks_holder_yet));
                });
          }
        });
  }

  public void showSection(int section) {
    binding.loading.setVisibility(View.GONE);
    binding.info.setVisibility(View.GONE);
    binding.blocksHolder.setVisibility(View.GONE);
    switch (section) {
      case 1:
        binding.loading.setVisibility(View.VISIBLE);
        break;
      case 2:
        binding.info.setVisibility(View.VISIBLE);
        break;
      case 3:
        binding.blocksHolder.setVisibility(View.VISIBLE);
        break;
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  private void saveBlocksHolderList() {
    Executor executor = Executors.newSingleThreadExecutor();
    executor.execute(
        () -> {
          try {
            BLOCKS.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(BLOCKS);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(blocksHolderList);
            fos.close();
            oos.close();
            finish();
          } catch (Exception e) {
            runOnUiThread(
                () -> {
                  Toast.makeText(
                          BlocksHolderManagerActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
                      .show();
                });
          }
        });
  }

  @Override
  public void onBackPressed() {
    saveBlocksHolderList();
  }
}
