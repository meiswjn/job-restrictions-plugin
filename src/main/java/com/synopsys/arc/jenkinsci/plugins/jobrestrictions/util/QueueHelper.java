/*
 * The MIT License
 *
 * Copyright 2013 Oleg Nenashev, Synopsys Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.synopsys.arc.jenkinsci.plugins.jobrestrictions.util;

import javax.annotation.Nonnull;

import hudson.model.Item;
import hudson.model.Job;
import hudson.model.Queue;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

/**
 * Provides additional for Queue objects.
 * @author Oleg Nenashev
 */
@Restricted(NoExternalUse.class)
public class QueueHelper {

    //TODO: Optimize by StringBuilder
    /**
     * Generates job-style project name for the buildable item.
     * @deprecated as of 0.9. Use {@link #getName(Queue.BuildableItem, boolean)}.
     * @param item Item, for which the name should be retrieved.
     * @return String in the {@link Job#getFullName()} format (Folder/SubFolder/.../Pipeline).
     */
    @Deprecated
    public static String getFullName(@Nonnull Queue.BuildableItem item) {
        return getName(item, false);
    }

    //TODO: Optimize by StringBuilder
    /**
     * Generates job-style project name for the buildable item.
     * @param item Item, for which the name should be retrieved.
     * @param shortName Boolean whether to ignore the path (Folders) and only match the pipeline's name.
     * @return String in the {@link Job#getFullName()} format 'Folder/SubFolder/.../Pipeline'. If shortName is true, only 'Pipeline'.
     */
    static String getName(@Nonnull Queue.BuildableItem item, boolean shortName) {
        Queue.Task current = item.task;
        String res = getItemName(current, shortName);

        if (!shortName) {
            // this is only executed if we didn't call Item.getFullName() in getItemName
            while (!(current instanceof Item)) {
                Queue.Task parent = current.getOwnerTask();
                if (parent == current || parent == null) {
                    break;
                }
                res = getItemName(parent, shortName) + "/" + res;
                current = parent;
            }
        }
        return res;
    }

    private static String getItemName(Queue.Task task, boolean shortName) {
        if (shortName || (!(task instanceof Item))) {
            return task.getName();
        }
        return ((Item) task).getFullName();
    }
}
