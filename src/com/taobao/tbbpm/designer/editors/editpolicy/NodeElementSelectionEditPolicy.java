package com.taobao.tbbpm.designer.editors.editpolicy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.handles.NonResizableHandleKit;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.handles.ResizeHandle;

import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.handler.impl.ERDiagramMoveHandle;
import com.taobao.tbbpm.designer.editors.handler.impl.ERDiagramResizeHandle;

public class NodeElementSelectionEditPolicy extends ResizableEditPolicy {
	@Override
	protected List createSelectionHandles() {
		List selectedEditParts = getHost().getViewer().getSelectedEditParts();
		if (selectedEditParts.size() == 1) {
			ElementNode currentElement = (ElementNode) getHost()
					.getModel();
			getHost().getRoot().getContents().refresh();
		}

		List list = new ArrayList();

		int directions = getResizeDirections();

		if (directions != 0) {
			if (directions != -1) {
				list.add(new ERDiagramMoveHandle((GraphicalEditPart) getHost()));

				if ((directions & 0x10) != 0)
					ResizableHandleKit.addHandle((GraphicalEditPart) getHost(),
							list, 16);
				else {
					NonResizableHandleKit.addHandle(
							(GraphicalEditPart) getHost(), list, 16);
				}

				if ((directions & 0x14) == 20)
					ResizableHandleKit.addHandle((GraphicalEditPart) getHost(),
							list, 20);
				else {
					NonResizableHandleKit.addHandle(
							(GraphicalEditPart) getHost(), list, 20);
				}

				if ((directions & 0x4) != 0)
					ResizableHandleKit.addHandle((GraphicalEditPart) getHost(),
							list, 4);
				else {
					NonResizableHandleKit.addHandle(
							(GraphicalEditPart) getHost(), list, 4);
				}

				if ((directions & 0xC) == 12)
					ResizableHandleKit.addHandle((GraphicalEditPart) getHost(),
							list, 12);
				else {
					NonResizableHandleKit.addHandle(
							(GraphicalEditPart) getHost(), list, 12);
				}

				if ((directions & 0x8) != 0)
					ResizableHandleKit.addHandle((GraphicalEditPart) getHost(),
							list, 8);
				else {
					NonResizableHandleKit.addHandle(
							(GraphicalEditPart) getHost(), list, 8);
				}

				if ((directions & 0x9) == 9)
					ResizableHandleKit.addHandle((GraphicalEditPart) getHost(),
							list, 9);
				else {
					NonResizableHandleKit.addHandle(
							(GraphicalEditPart) getHost(), list, 9);
				}

				if ((directions & 0x1) != 0)
					ResizableHandleKit.addHandle((GraphicalEditPart) getHost(),
							list, 1);
				else {
					NonResizableHandleKit.addHandle(
							(GraphicalEditPart) getHost(), list, 1);
				}

				if ((directions & 0x11) == 17)
					ResizableHandleKit.addHandle((GraphicalEditPart) getHost(),
							list, 17);
				else
					NonResizableHandleKit.addHandle(
							(GraphicalEditPart) getHost(), list, 17);
			} else {
				addHandles((GraphicalEditPart) getHost(), list);
			}
		}
		return list;
	}

	private void addHandles(GraphicalEditPart part, List handles) {
		handles.add(new ERDiagramMoveHandle(part));
		handles.add(createHandle(part, 16));
		handles.add(createHandle(part, 20));
		handles.add(createHandle(part, 4));
		handles.add(createHandle(part, 12));
		handles.add(createHandle(part, 8));
		handles.add(createHandle(part, 9));
		handles.add(createHandle(part, 1));
		handles.add(createHandle(part, 17));
	}

	private Handle createHandle(GraphicalEditPart owner, int direction) {
		ResizeHandle handle = new ERDiagramResizeHandle(owner, direction);
		return handle;
	}

}
