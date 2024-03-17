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

package com.block.web.builder.ui.bottomsheet;

import android.code.editor.common.interfaces.FileDeleteListener;
import android.code.editor.common.utils.FileDeleteUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.block.web.builder.databinding.LayoutBottomsheetProjectOperationBinding;
import com.block.web.builder.listeners.TaskListener;
import com.block.web.builder.ui.activities.MainActivity;
import com.block.web.builder.ui.dialogs.project.DeleteProjectDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ProjectOperationBottomSheet extends BottomSheetDialogFragment {
  public int position;
  public ArrayList<HashMap<String, Object>> projectList;
  public MainActivity mMainActivity;

  public ProjectOperationBottomSheet(
      int position, ArrayList<HashMap<String, Object>> projectList, MainActivity mMainActivity) {
    this.position = position;
    this.projectList = projectList;
    this.mMainActivity = mMainActivity;
  }

  @Override
  public View onCreateView(LayoutInflater inflator, ViewGroup layout, Bundle bundle) {
    LayoutBottomsheetProjectOperationBinding binding =
        LayoutBottomsheetProjectOperationBinding.inflate(inflator);
    binding.deleteFile.setOnClickListener(
        v -> {
          new DeleteProjectDialog(
                  mMainActivity,
                  projectList.get(position),
                  new TaskListener() {

                    @Override
                    public void onSuccess(Object result) {
                      FileDeleteUtils.delete(
                          ((File) projectList.get(position).get("Path")),
                          new FileDeleteListener() {
                            @Override
                            public void onProgressUpdate(int deleteDone) {}

                            @Override
                            public void onTotalCount(int total) {}

                            @Override
                            public void onDeleting(File path) {}

                            @Override
                            public void onDeleteComplete(File path) {}

                            @Override
                            public void onTaskComplete() {
                              mMainActivity.loadProjectInList();
                            }
                          },
                          true,
                          mMainActivity);
                    }
                  })
              .create()
              .show();
        });

    return binding.getRoot();
  }
}
