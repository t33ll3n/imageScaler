import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;

public class Drop implements DropTargetListener{
	 private Scaler scaler;
	 private Window window;
	
	public Drop(Scaler scaler, Window window){
		this.scaler = scaler;
		this.window = window;
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent dd) {
		// TODO Auto-generated method stub
	}

	@Override
	public void drop(DropTargetDropEvent dd) {
		
		dd.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		
		Transferable t = dd.getTransferable();
		
		DataFlavor[] df = t.getTransferDataFlavors();
		
//		System.out.println(df);
		
		for (DataFlavor f : df){
			try {
				if (f.isFlavorJavaFileListType()){
					java.util.List<File> files = (java.util.List<File>) t.getTransferData(f);
//					System.out.println(files);
					window.seleced.setText(scaler.dropPhotos(files) + " images selected");
				
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dd) {
		
	}

}
