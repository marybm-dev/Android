package cs4521.mmartinez.navdrawer;

public class DrawerItem {
	 
    String ItemName;
    int imgResID;

    public DrawerItem(String itemName, int imgResID) {
          super();
          ItemName = itemName;
          this.imgResID = imgResID;
    }
    
    public DrawerItem(String itemName) {
    	super();
    	ItemName = itemName;
    }

    public String getItemName() {
          return ItemName;
    }
    public void setItemName(String itemName) {
          ItemName = itemName;
    }
    public int getImgResID() {
          return imgResID;
    }
    public void setImgResID(int imgResID) {
          this.imgResID = imgResID;
    }

}