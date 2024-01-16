package com.block.web.builder.objects;

import com.block.web.builder.objects.blockcontent.BooleanContent;
import com.block.web.builder.objects.blockcontent.SourceContent;
import com.block.web.builder.utils.CodeReplacer;
import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable, Cloneable {
  private static final long serialVersionUID = 428383837L;
  private String color;
  private String name;
  private ArrayList<BlockContent> blockContent;
  private int blockType;
  private String rawCode;
  private String returns;
  private boolean enableSideAttachableBlock;
  private ArrayList<Block> sideAttachableBlock;
  private String[] tags;

  public String getColor() {
    if (this.color != null) {
      return this.color;
    }
    return "#000000";
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getName() {
    if (this.name != null) {
      return this.name;
    }
    return "";
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getBlockType() {
    return this.blockType;
  }

  public void setBlockType(int BlockType) {
    this.blockType = BlockType;
  }

  public String getCode() {
    String blockRawCode = new String(getRawCode());
    for (int i = 0; i < getBlockContent().size(); ++i) {
      if (getBlockContent().get(i) instanceof ComplexBlockContent) {
        blockRawCode =
            blockRawCode.replace(
                CodeReplacer.getReplacer(((ComplexBlockContent) getBlockContent().get(i)).getId()),
                ((ComplexBlockContent) getBlockContent().get(i)).getCode());
      }
    }

    StringBuilder attachementBlocksCode = new StringBuilder();

    for (int i = 0; i < getSideAttachableBlock().size(); ++i) {
      if (getSideAttachableBlock().get(i).getBlockType() == BlockType.sideAttachableBlock) {
        attachementBlocksCode.append(getSideAttachableBlock().get(i).getCode());
        attachementBlocksCode.append(" ");
      }
    }

    blockRawCode.replace(
        CodeReplacer.getReplacer("attachementBlock"), attachementBlocksCode.toString());

    blockRawCode = CodeReplacer.removeBlockWebBuilderString(blockRawCode);
    return new String(blockRawCode);
  }

  public String getRawCode() {
    return new String(this.rawCode);
  }

  public void setRawCode(String rawCode) {
    this.rawCode = rawCode;
  }

  public final class BlockType {
    public static final int defaultBlock = 0;
    public static final int complexBlock = 1;
    public static final int doubleComplexBlock = 2;
    public static final int returnWithTypeBoolean = 3;
    public static final int sideAttachableBlock = 4;
  }

  public ArrayList<BlockContent> getBlockContent() {
    return blockContent;
  }

  public void setBlockContent(ArrayList<BlockContent> blockContent) {
    this.blockContent = blockContent;
  }

  public String getReturns() {
    if (returns != null) {
      return this.returns;
    }
    return new String("");
  }

  public void setReturns(String returns) {
    this.returns = returns;
  }

  public ArrayList<Block> getSideAttachableBlock() {
    return this.sideAttachableBlock;
  }

  public void setSideAttachableBlock(ArrayList<Block> sideAttachableBlock) {
    this.sideAttachableBlock = sideAttachableBlock;
  }

  public boolean getEnableSideAttachableBlock() {
    return this.enableSideAttachableBlock;
  }

  public void setEnableSideAttachableBlock(boolean enableSideAttachableBlock) {
    this.enableSideAttachableBlock = enableSideAttachableBlock;
  }

  @Override
  public Block clone() throws CloneNotSupportedException {
    String mColor;
    if (getColor() != null) {
      mColor = new String(getColor());
    } else {
      mColor = new String("");
    }
    String mName;
    if (getName() != null) {
      mName = new String(getName());
    } else {
      mName = new String("");
    }
    ArrayList<BlockContent> mBlockContent;
    if (getBlockContent() != null) {
      mBlockContent = new ArrayList<BlockContent>();
      for (int i = 0; i < getBlockContent().size(); ++i) {
        if (getBlockContent().get(i) instanceof ComplexBlockContent) {
          if (getBlockContent().get(i) instanceof SourceContent) {
            mBlockContent.add(((SourceContent) getBlockContent().get(i)).clone());
          } else if (getBlockContent().get(i) instanceof BooleanContent) {
            mBlockContent.add(((BooleanContent) getBlockContent().get(i)).clone());
          }
        } else if (getBlockContent().get(i) instanceof BlockContent) {
          mBlockContent.add(((BlockContent) getBlockContent().get(i)).clone());
        }
      }
    } else {
      mBlockContent = new ArrayList<BlockContent>();
    }
    int mBlockType;
    if (getBlockType() != 0) {
      mBlockType = new Integer(getBlockType());
    } else {
      mBlockType = 0;
    }
    String mRawCode;
    if (getRawCode() != null) {
      mRawCode = new String(getRawCode());
    } else {
      mRawCode = new String("");
    }
    String mReturns;
    if (getReturns() != null) {
      mReturns = new String(getReturns());
    } else {
      mReturns = new String("");
    }
    ArrayList<Block> mSideAttachableBlock;
    if (getSideAttachableBlock() != null) {
      mSideAttachableBlock = new ArrayList<Block>();
      for (int i = 0; i < getSideAttachableBlock().size(); ++i) {
        if (getSideAttachableBlock().get(i) instanceof DoubleComplexBlock) {
          mSideAttachableBlock.add(((DoubleComplexBlock) getSideAttachableBlock().get(i)).clone());
        } else if (getSideAttachableBlock().get(i) instanceof ComplexBlock) {
          mSideAttachableBlock.add(((ComplexBlock) getSideAttachableBlock().get(i)).clone());
        } else if (getSideAttachableBlock().get(i) instanceof Block) {
          mSideAttachableBlock.add(getSideAttachableBlock().get(i).clone());
        }
      }
    } else {
      mSideAttachableBlock = new ArrayList<Block>();
    }
    boolean mEnableSideAttachableBlock = new Boolean(getEnableSideAttachableBlock());
    Block mBlock = new Block();
    mBlock.setColor(mColor);
    mBlock.setName(mName);
    mBlock.setBlockContent(mBlockContent);
    mBlock.setBlockType(mBlockType);
    mBlock.setRawCode(mRawCode);
    mBlock.setReturns(mReturns);
    mBlock.setEnableSideAttachableBlock(mEnableSideAttachableBlock);
    mBlock.setSideAttachableBlock(mSideAttachableBlock);
    return mBlock;
  }

  public String[] getTags() {
    return this.tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }
}
