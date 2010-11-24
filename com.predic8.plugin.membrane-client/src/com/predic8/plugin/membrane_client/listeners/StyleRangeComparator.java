/* Copyright 2010 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.plugin.membrane_client.listeners;

import java.util.Comparator;

import org.eclipse.swt.custom.StyleRange;

public class StyleRangeComparator implements Comparator<StyleRange> {

	public int compare(StyleRange r1, StyleRange r2) {
		if (r1.start < r2.start)
			return -1;
		if (r1.start == r2.start)
			return 0;
		return 1;
	}
	
}
