/*
 *    Copyright 2017 tiagohm
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package br.tiagohm.markdownview.ext.video.internal;

import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.parser.block.NodePostProcessor;
import com.vladsch.flexmark.parser.block.NodePostProcessorFactory;
import com.vladsch.flexmark.util.NodeTracker;
import com.vladsch.flexmark.util.options.DataHolder;
import com.vladsch.flexmark.util.sequence.BasedSequence;

import br.tiagohm.markdownview.ext.video.VideoLink;

public class VideoLinkNodePostProcessor extends NodePostProcessor {
    public VideoLinkNodePostProcessor(DataHolder options) {
    }

    @Override
    public void process(NodeTracker state, Node node) {
        if (node instanceof Link) {
            Node previous = node.getPrevious();

            if (previous instanceof Text) {
                final BasedSequence chars = previous.getChars();

                //Se o nó anterior termina com '@' e é seguido pelo Link
                if (chars.endsWith("@") && chars.isContinuedBy(node.getChars())) {
                    //Remove o caractere '@' do nó anterior.
                    previous.setChars(chars.subSequence(0, chars.length() - 1));
                    VideoLink videoLink = new VideoLink((Link) node);
                    videoLink.takeChildren(node);
                    node.unlink();
                    previous.insertAfter(videoLink);
                    state.nodeRemoved(node);
                    state.nodeAddedWithChildren(videoLink);
                }
            }
        }
    }

    public static class Factory extends NodePostProcessorFactory {
        public Factory(DataHolder options) {
            super(false);

            addNodes(Link.class);
        }

        @Override
        public NodePostProcessor create(Document document) {
            return new VideoLinkNodePostProcessor(document);
        }
    }
}
