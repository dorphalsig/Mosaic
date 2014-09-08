package com.ceteva.forms.views;

import java.util.Vector;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import uk.ac.mdx.xmf.swt.client.Commandable;
import uk.ac.mdx.xmf.swt.client.ComponentWithControl;
import uk.ac.mdx.xmf.swt.client.Draggable;
import uk.ac.mdx.xmf.swt.client.Droppable;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class FormElement.
 */
abstract class FormElement implements Commandable, ComponentWithControl,
		Draggable, Droppable {

	/** The identity. */
	private String identity = "";
	
	/** The handler. */
	public EventHandler handler = null;
	
	/** The events enabled. */
	boolean eventsEnabled = true;
	
	/** The drag enabled. */
	boolean dragEnabled = false;
	
	/** The drop enabled. */
	boolean dropEnabled = false;

	/**
	 * Instantiates a new form element.
	 *
	 * @param identity the identity
	 */
	public FormElement(String identity) {
		setIdentity(identity);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ComponentWithIdentity#getIdentity()
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * Disable events.
	 */
	public void disableEvents() {
		eventsEnabled = false;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Commandable#processMessage(xos.Message)
	 */
	public boolean processMessage(Message message) {
		if (message.args[0].hasStrValue(identity)) {
			if (message.hasName("enableDrag") && message.arity == 1) {
				enableDrag();
				return true;
			}
		}
		if (message.args[0].hasStrValue(identity)) {
			if (message.hasName("enableDrop") && message.arity == 1) {
				enableDrop();
				return true;
			}
		}
		return false;
	}

	/**
	 * Raise event.
	 *
	 * @param m the m
	 */
	void raiseEvent(Message m) {
		if (eventsEnabled)
			handler.raiseEvent(m);
	}

	// DRAG

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Draggable#dragEnabled()
	 */
	public boolean dragEnabled() {
		return dragEnabled;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Draggable#enableDrag()
	 */
	public void enableDrag() {
		if (!dragEnabled()) {
			setDraggable();
		}
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Draggable#setDraggable()
	 */
	public void setDraggable() {
		dragEnabled = true;
		final FormElement formElement = this;
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		DragSource source = new DragSource(this.getControl(), operations);
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		source.setTransfer(types);

		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				formElement.dragStart(event);
			}

			public void dragSetData(DragSourceEvent event) {
				formElement.dragSetData(event);
			}

			public void dragFinished(DragSourceEvent event) {
				formElement.dragFinished(event);
			}
		});
	}

	/**
	 * Drag start.
	 *
	 * @param event the event
	 */
	public void dragStart(DragSourceEvent event) {
		// System.out.println("dragStart: '" + getIdentity() + "'");
	}

	/**
	 * Drag set data.
	 *
	 * @param event the event
	 */
	public void dragSetData(DragSourceEvent event) {
		// System.out.println("dragSetData: '" + getIdentity() + "'");
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			Vector dragIds = new Vector();
			dragIds.add(getIdentity());
			String dragIdsString = dragIds.toString();
			event.data = dragIdsString;
		}
	}

	/**
	 * Drag finished.
	 *
	 * @param event the event
	 */
	public void dragFinished(DragSourceEvent event) {
		// System.out.println("dragFinished: '" + getIdentity() + "'");
	}

	// DROP

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Droppable#dropEnabled()
	 */
	public boolean dropEnabled() {
		return dropEnabled;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Droppable#enableDrop()
	 */
	public void enableDrop() {
		if (!dropEnabled()) {
			setDroppable();
		}
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Droppable#setDroppable()
	 */
	public void setDroppable() {
		dropEnabled = true;
		final FormElement formElement = this;
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget target = new DropTarget(this.getControl(), operations);
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		target.setTransfer(types);

		target.addDropListener(new DropTargetListener() {
			public void dragEnter(DropTargetEvent event) {
				formElement.dragEnter(event);
			}

			public void dragOver(DropTargetEvent event) {
				formElement.dragOver(event);
			}

			public void dragOperationChanged(DropTargetEvent event) {
				formElement.dragOperationChanged(event);
			}

			public void dragLeave(DropTargetEvent event) {
				formElement.dragLeave(event);
			}

			public void dropAccept(DropTargetEvent event) {
				formElement.dropAccept(event);
			}

			public void drop(DropTargetEvent event) {
				formElement.drop(event);
			}
		});
	}

	/**
	 * Drag enter.
	 *
	 * @param event the event
	 */
	public void dragEnter(DropTargetEvent event) {
		// System.out.println("dragEnter: '" + getIdentity() + "'");
	}

	/**
	 * Drag over.
	 *
	 * @param event the event
	 */
	public void dragOver(DropTargetEvent event) {
		// System.out.println("dragOver: '" + getIdentity() + "'");
	}

	/**
	 * Drag operation changed.
	 *
	 * @param event the event
	 */
	public void dragOperationChanged(DropTargetEvent event) {
		// System.out.println("dragOperationChanged: '" + getIdentity() + "'");
	}

	/**
	 * Drag leave.
	 *
	 * @param event the event
	 */
	public void dragLeave(DropTargetEvent event) {
		// System.out.println("dragLeave: '" + getIdentity() + "'");
	}

	/**
	 * Drop accept.
	 *
	 * @param event the event
	 */
	public void dropAccept(DropTargetEvent event) {
		// System.out.println("dropAccept: '" + getIdentity() + "'");
	}

	/**
	 * Drop.
	 *
	 * @param event the event
	 */
	public void drop(DropTargetEvent event) {
		// System.out.println("drop: '" + getIdentity() + "'");
		if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
			String dragIdsString = (String) event.data;
			String dropId = getIdentity();
			String operation = getDropOperation(event.detail);
			// System.out.println("drop: '" + dragIdsString + "->" + dropId +
			// "'");
			raiseDragAndDropEvent(dropId, operation, dragIdsString);
		}
	}

	/**
	 * Gets the drop operation.
	 *
	 * @param eventDetail the event detail
	 * @return the drop operation
	 */
	public String getDropOperation(int eventDetail) {
		if (eventDetail == DND.DROP_COPY)
			return "copy";
		if (eventDetail == DND.DROP_MOVE)
			return "move";
		return "default";
	}

	/**
	 * Raise drag and drop event.
	 *
	 * @param dropId the drop id
	 * @param operation the operation
	 * @param dragIdsString the drag ids string
	 */
	public void raiseDragAndDropEvent(String dropId, String operation,
			String dragIdsString) {
		Message m = handler.newMessage("dragAndDrop", 3);
		Value v1 = new Value(dropId);
		Value v2 = new Value(operation);
		Value v3 = new Value(dragIdsString);
		m.args[0] = v1;
		m.args[1] = v2;
		m.args[2] = v3;
		raiseEvent(m);
	}

	/**
	 * Raise accept drop event.
	 *
	 * @param dropId the drop id
	 * @param operation the operation
	 * @param dragIdsString the drag ids string
	 */
	public void raiseAcceptDropEvent(String dropId, String operation,
			String dragIdsString) {
		Message m = handler.newMessage("acceptDrop", 3);
		Value v1 = new Value(dropId);
		Value v2 = new Value(operation);
		Value v3 = new Value(dragIdsString);
		m.args[0] = v1;
		m.args[1] = v2;
		m.args[2] = v3;
		raiseEvent(m);
	}

	/**
	 * Sets the identity.
	 *
	 * @param identity the new identity
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

}