package github.tornaco.android.thanos.dashboard;

import java.util.List;

public class TileGroup {
    private StatusHeaderInfo headerInfo;
    private StatusFooterInfo footerInfo;

    private String title;
    private List<Tile> tiles;

    public TileGroup(StatusFooterInfo footerInfo) {
        this.footerInfo = footerInfo;
    }

    public TileGroup(StatusHeaderInfo headerInfo) {
        this.headerInfo = headerInfo;
    }

    public TileGroup(String title, List<Tile> tiles) {
        this.title = title;
        this.tiles = tiles;
    }

    public boolean hasAtLeastOneTile() {
        return tiles != null && !tiles.isEmpty();
    }

    public StatusHeaderInfo getHeaderInfo() {
        return this.headerInfo;
    }

    public StatusFooterInfo getFooterInfo() {
        return this.footerInfo;
    }

    public String getTitle() {
        return this.title;
    }

    public List<Tile> getTiles() {
        return this.tiles;
    }
}
