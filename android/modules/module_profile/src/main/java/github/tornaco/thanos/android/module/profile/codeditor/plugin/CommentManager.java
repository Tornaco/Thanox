package github.tornaco.thanos.android.module.profile.codeditor.plugin;

import android.text.Editable;
import android.widget.EditText;

public class CommentManager {

    private final EditText textView;
    private final Editable editable;

    private String commentStart;
    private int commentStartLength;

    private String commentEnd;
    private int commendEndLength;

    public CommentManager(EditText textView) {
        this.textView = textView;
        this.editable = textView.getText();
        this.commentStart = "";
        this.commentStartLength = 0;
        this.commentEnd = "";
        this.commendEndLength = 0;
    }

    public CommentManager(EditText textView, String commentStart, String commentEnd) {
        this.textView = textView;
        this.editable = textView.getText();
        this.commentStart = commentStart;
        this.commentStartLength = commentStart.length();
        this.commentEnd = commentEnd;
        this.commendEndLength = commentEnd.length();
    }

    public void commentSelected() {
        int start = textView.getSelectionStart();
        int end = textView.getSelectionEnd();
        if (start != end) {
            String[] lines = editable.subSequence(start, end).toString().split("\n");
            StringBuilder builder = new StringBuilder();
            int len = lines.length;
            for (int i = 0; i < len; i++) {
                String line = lines[i];
                if (!line.startsWith(commentStart)) builder.append(commentStart);
                builder.append(line);
                if (!line.endsWith(commentEnd)) builder.append(commentEnd);
                if (i != len - 1) builder.append("\n");
            }
            editable.replace(start, end, builder);
        }
    }

    public void unCommentSelected() {
        int start = textView.getSelectionStart();
        int end = textView.getSelectionEnd();
        if (start != end) {
            String[] lines = editable.subSequence(start, end).toString().split("\n");
            StringBuilder builder = new StringBuilder();
            int len = lines.length;
            for (int i = 0; i < len; i++) {
                String line = lines[i];
                if (line.startsWith(commentStart) && line.endsWith(commentEnd))
                    builder.append(line.substring(commentStartLength, line.length() - commendEndLength));
                else builder.append(line);
                if (i != len - 1) builder.append("\n");
            }
            editable.replace(start, end, builder);
        }
    }

    public void setCommentStart(String comment) {
        this.commentStart = comment;
        this.commentStartLength = comment.length();
    }

    public void setCommendEnd(String comment) {
        this.commentEnd = comment;
        this.commendEndLength = comment.length();
    }
}