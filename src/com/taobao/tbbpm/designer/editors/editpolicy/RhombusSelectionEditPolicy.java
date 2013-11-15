package com.taobao.tbbpm.designer.editors.editpolicy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.ResizableHandleKit;

import com.taobao.tbbpm.designer.editors.figure.Rhombus;
import com.taobao.tbbpm.designer.editors.figure.TbbpmRhombusLineBorder;

public class RhombusSelectionEditPolicy extends ResizableEditPolicy {
	@Override
	protected List createSelectionHandles() {
		 List list = new ArrayList();  
		    //添加选择框  
		    //ResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(), list);  
		    list.add(new MoveHandle((GraphicalEditPart) getHost()) {  
		        @Override
				protected void initialize() {  
		            super.initialize();  
		            setBorder(new TbbpmRhombusLineBorder());  
		        }  
		    });  
		      
		    //添加控制柄  
		    ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.EAST);  
		    ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH);  
		    ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.WEST);  
		    ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH);  
		    return list;  
	}

	@Override
	protected IFigure createDragSourceFeedbackFigure() {  
	    //用椭圆替代矩形  
	    //RectangleFigure r = new RectangleFigure();  
		Rhombus r = new Rhombus();  
	    FigureUtilities.makeGhostShape(r);  
	    r.setLineStyle(Graphics.LINE_DOT);  
	    r.setForegroundColor(ColorConstants.white);  
	    r.setBounds(getInitialFeedbackBounds());  
	    addFeedback(r);  
	    return r;  
	}  

}
