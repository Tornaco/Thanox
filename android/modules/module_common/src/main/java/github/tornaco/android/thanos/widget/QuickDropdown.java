package github.tornaco.android.thanos.widget;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import github.tornaco.android.thanos.core.util.function.Function;
import util.Consumer;

public class QuickDropdown {
  public static void show(
      Context context,
      View anchor,
      Function<Integer, String> titleProvider,
      Consumer<Integer> onSelect) {
    PopupMenu menu = new PopupMenu(context, anchor);
    for (int i = 0; i < 1024; i++) {
      String title = titleProvider.apply(i);
      if (title == null) break;
      menu.getMenu().add(1000, i, Menu.NONE, title);
    }
    menu.setOnMenuItemClickListener(
        new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem item) {
            onSelect.accept(item.getItemId());
            return true;
          }
        });
    menu.show();
  }
}
