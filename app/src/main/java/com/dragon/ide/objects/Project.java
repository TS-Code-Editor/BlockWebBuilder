package com.dragon.ide.objects;

import java.io.Serializable;

public class Project implements Serializable {
  public static final long serialVersionUID = 428383834L;
  public String projectName;

  public String getProjectName() {
    return this.projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }
}
