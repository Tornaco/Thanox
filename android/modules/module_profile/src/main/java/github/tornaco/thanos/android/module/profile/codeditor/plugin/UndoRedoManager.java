package github.tornaco.thanos.android.module.profile.codeditor.plugin;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import java.util.LinkedList;

public class UndoRedoManager {

    private final TextView textView;
    private final EditHistory editHistory;
    private final TextChangeWatcher textChangeWatcher;

    private boolean isUndoOrRedo = false;

    public UndoRedoManager(TextView textView) {
        this.textView = textView;
        editHistory = new EditHistory();
        textChangeWatcher = new TextChangeWatcher();
    }

    public void undo() {
        EditNode edit = editHistory.getPrevious();
        if (edit == null) return;

        Editable text = textView.getEditableText();
        int start = edit.start;
        int end = start + (edit.after != null ? edit.after.length() : 0);

        isUndoOrRedo = true;
        text.replace(start, end, edit.before);
        isUndoOrRedo = false;

        UnderlineSpan[] underlineSpans = text.getSpans(0, text.length(), UnderlineSpan.class);
        for (Object span : underlineSpans) text.removeSpan(span);

        Selection.setSelection(text, edit.before == null ? start : (start + edit.before.length()));
    }

    public void redo() {
        EditNode edit = editHistory.getNext();
        if (edit == null) return;

        Editable text = textView.getEditableText();
        int start = edit.start;
        int end = start + (edit.before != null ? edit.before.length() : 0);

        isUndoOrRedo = true;
        text.replace(start, end, edit.after);
        isUndoOrRedo = false;

        UnderlineSpan[] underlineSpans = text.getSpans(0, text.length(), UnderlineSpan.class);
        for (Object span : underlineSpans) text.removeSpan(span);

        Selection.setSelection(text, edit.after == null ? start : (start + edit.after.length()));
    }

    public void connect() {
        textView.addTextChangedListener(textChangeWatcher);
    }

    public void disconnect() {
        textView.removeTextChangedListener(textChangeWatcher);
    }

    public void setMaxHistorySize(int maxSize) {
        editHistory.setMaxHistorySize(maxSize);
    }

    public void clearHistory() {
        editHistory.clear();
    }

    public boolean canUndo() {
        return editHistory.position > 0;
    }

    public boolean canRedo() {
        return editHistory.position < editHistory.historyList.size();
    }

    private static final class EditHistory {

        private int position = 0;
        private int maxHistorySize = -1;

        private final LinkedList<EditNode> historyList = new LinkedList<>();

        private void clear() {
            position = 0;
            historyList.clear();
        }

        private void add(EditNode item) {
            while (historyList.size() > position) historyList.removeLast();
            historyList.add(item);
            position++;
            if (maxHistorySize >= 0) trimHistory();
        }

        private void setMaxHistorySize(int maxHistorySize) {
            this.maxHistorySize = maxHistorySize;
            if (this.maxHistorySize >= 0) trimHistory();
        }

        private void trimHistory() {
            while (historyList.size() > maxHistorySize) {
                historyList.removeFirst();
                position--;
            }

            if (position < 0) position = 0;
        }

        private EditNode getCurrent() {
            if (position == 0) return null;
            return historyList.get(position - 1);
        }

        private EditNode getPrevious() {
            if (position == 0) return null;
            position--;
            return historyList.get(position);
        }

        private EditNode getNext() {
            if (position >= historyList.size()) return null;
            EditNode item = historyList.get(position);
            position++;
            return item;
        }
    }

    private static final class EditNode {

        private int start;
        private CharSequence before;
        private CharSequence after;

        public EditNode(int start, CharSequence before, CharSequence after) {
            this.start = start;
            this.before = before;
            this.after = after;
        }
    }

    private enum ActionType {
        INSERT, DELETE, PASTE, NOT_DEF;
    }

    private final class TextChangeWatcher implements TextWatcher {

        private CharSequence beforeChange;
        private CharSequence afterChange;
        private ActionType lastActionType = ActionType.NOT_DEF;

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (isUndoOrRedo) return;
            beforeChange = s.subSequence(start, start + count);
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isUndoOrRedo) return;
            afterChange = s.subSequence(start, start + count);
            makeBatch(start);
        }

        private void makeBatch(int start) {
            ActionType action = getActionType();
            EditNode currentNode = editHistory.getCurrent();
            if (lastActionType != action || ActionType.PASTE == action || currentNode == null) {
                editHistory.add(new EditNode(start, beforeChange, afterChange));
            } else {
                if (action == ActionType.DELETE) {
                    currentNode.start = start;
                    currentNode.before = TextUtils.concat(beforeChange, currentNode.before);
                } else {
                    currentNode.after = TextUtils.concat(currentNode.after, afterChange);
                }
            }
            lastActionType = action;
        }

        private ActionType getActionType() {
            if (!TextUtils.isEmpty(beforeChange) && TextUtils.isEmpty(afterChange)) {
                return ActionType.DELETE;
            } else if (TextUtils.isEmpty(beforeChange) && !TextUtils.isEmpty(afterChange)) {
                return ActionType.INSERT;
            } else {
                return ActionType.PASTE;
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }
}
