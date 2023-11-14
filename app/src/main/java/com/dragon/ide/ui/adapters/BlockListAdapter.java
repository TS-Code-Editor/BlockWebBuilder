package com.dragon.ide.ui.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.dragon.ide.R;
import com.dragon.ide.objects.Block;
import com.dragon.ide.objects.ComplexBlock;
import com.dragon.ide.objects.DoubleComplexBlock;
import com.dragon.ide.ui.activities.EventEditorActivity;
import com.dragon.ide.ui.view.BlockDefaultView;
import com.dragon.ide.ui.view.ComplexBlockView;
import com.dragon.ide.utils.Utils;
import java.util.ArrayList;

public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.ViewHolder> {

  public ArrayList<Block> list;
  public Activity activity;

  public BlockListAdapter(ArrayList<Block> _arr, Activity activity) {
    list = _arr;
    this.activity = activity;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LinearLayout _v = new LinearLayout(activity);
    RecyclerView.LayoutParams _lp =
        new RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    _v.setLayoutParams(_lp);
    return new ViewHolder(_v);
  }

  @Override
  public void onBindViewHolder(ViewHolder _holder, int _position) {
    LinearLayout v = (LinearLayout) _holder.itemView;
    v.setPadding(8, 8, 8, 8);
    if (!(list.get(_position) instanceof DoubleComplexBlock)) {

      if ((list.get(_position) instanceof ComplexBlock)) {
        if (list.get(_position).getBlockType() == Block.BlockType.complexBlock) {
          ComplexBlockView complexBlockView = new ComplexBlockView(activity);
          complexBlockView.setComplexBlock((ComplexBlock) list.get(_position));
          v.addView(complexBlockView);
          complexBlockView.setOnLongClickListener(
              (view) -> {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(complexBlockView);

                addDragListener(
                    ((EventEditorActivity) activity).binding.relativeBlockListEditorArea,
                    (EventEditorActivity) activity,
                    ((ComplexBlock) list.get(_position)).getReturns(),
                    ((ComplexBlock) list.get(_position)).getBlockType());

                if (Build.VERSION.SDK_INT >= 24) {
                  complexBlockView.startDragAndDrop(data, shadow, complexBlockView, 1);
                } else {
                  complexBlockView.startDrag(data, shadow, complexBlockView, 1);
                }
                return false;
              });
        }
      } else if (list.get(_position) instanceof Block) {
        BlockDefaultView blockView = new BlockDefaultView(activity);
        blockView.setBlock(list.get(_position));
        v.addView(blockView);
        blockView.setOnLongClickListener(
            (view) -> {
              ClipData data = ClipData.newPlainText("", "");
              View.DragShadowBuilder shadow = new View.DragShadowBuilder(blockView);

              addDragListener(
                  ((EventEditorActivity) activity).binding.relativeBlockListEditorArea,
                  (EventEditorActivity) activity,
                  ((Block) list.get(_position)).getReturns(),
                  ((Block) list.get(_position)).getBlockType());

              if (Build.VERSION.SDK_INT >= 24) {
                blockView.startDragAndDrop(data, shadow, blockView, 1);
              } else {
                blockView.startDrag(data, shadow, blockView, 1);
              }
              return false;
            });
      }
    }
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View v) {
      super(v);
    }
  }

  public static void addDragListener(
      ViewGroup view, View.OnDragListener listener, String returns, int type) {
    for (int i = 0; i < view.getChildCount(); ++i) {
      boolean isDropable = false;
      if (view.getChildAt(i) instanceof ViewGroup) {
        addDragListener((ViewGroup) view.getChildAt(i), listener, returns, type);
      }
      if (type == Block.BlockType.defaultBlock
          || type == Block.BlockType.complexBlock
          || type == Block.BlockType.doubleComplexBlock) {
        if (view.getChildAt(i).getTag() != null) {
          if (view.getChildAt(i).getTag() instanceof String) {
            if (((String) view.getChildAt(i).getTag()).equals("blockDroppingArea")) {
              view.getChildAt(i).setOnDragListener(listener);
              isDropable = true;
            }
          }
        }
      } else if (type == Block.BlockType.returnWithTypeBoolean) {
        if (view.getChildAt(i).getTag() != null) {
          if (view.getChildAt(i).getTag() instanceof String[]) {
            boolean containsTargetString = false;

            for (String str : (String[]) view.getChildAt(i).getTag()) {
              if (str.equals(returns)) {
                if (str.equals("boolean")) {
                  containsTargetString = true;
                  break;
                }
              }
            }
            if (containsTargetString) {
              view.getChildAt(i).setOnDragListener(listener);
              isDropable = true;
            }
          }
        }
      }
      if (!isDropable) {
        view.getChildAt(i).setOnDragListener(null);
      }
    }
  }
}
