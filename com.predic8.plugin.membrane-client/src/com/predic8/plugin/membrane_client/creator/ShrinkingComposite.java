package com.predic8.plugin.membrane_client.creator;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class ShrinkingComposite extends Composite {

	public ShrinkingComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		if (!isVisible()) {
            return new Point(0,0);
        } else {
           return super.computeSize(wHint, hHint, changed);
        }
	}
	
}
