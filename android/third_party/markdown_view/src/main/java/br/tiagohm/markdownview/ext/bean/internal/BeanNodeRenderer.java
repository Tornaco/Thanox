package br.tiagohm.markdownview.ext.bean.internal;

import android.util.Log;

import com.vladsch.flexmark.html.CustomNodeRenderer;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.util.options.DataHolder;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.ext.bean.Bean;
import br.tiagohm.markdownview.ext.bean.BeanExtension;

public class BeanNodeRenderer implements NodeRenderer {
    private final MarkdownView mMarkdownView;

    public BeanNodeRenderer(DataHolder options) {
        mMarkdownView = options.get(BeanExtension.BEAN_VIEW);
    }

    private static Object getBeanAttributeValue(String name, Object bean) {
        //Pega o tipo do bean.
        Class<?> beanClass = bean.getClass();
        //Logger.d("buscando %s em %s", name, beanClass.getSimpleName());
        //Pega todos o caminho do membro separadamente.
        String[] fieldNames = name.split("\\.");
        //O caminho é válido.
        if (fieldNames != null && fieldNames.length >= 1) {
            final String methodName = fieldNames[0];
            final String methodNameCamelCase = Character.toUpperCase(methodName.charAt(0)) +
                    methodName.substring(1);
            Method fieldMethod;
            //Tantar usar o xxx().
            try {
                fieldMethod = beanClass.getMethod(methodName);
                //Logger.d("encontrado %s", methodName);
            } catch (NoSuchMethodException e) {
                //Tentar usar o getXxx().
                try {
                    fieldMethod = beanClass.getMethod("get" + methodNameCamelCase);
                    //Logger.d("encontrado get%s", methodNameCamelCase);
                } catch (NoSuchMethodException e1) {
                    //Tentar usar o isXxx().
                    try {
                        fieldMethod = beanClass.getMethod("is" + methodNameCamelCase);
                        //Logger.d("encontrado is%", methodNameCamelCase);
                    } catch (NoSuchMethodException e2) {
                        Log.e("", Log.getStackTraceString(e2));
                        return null;
                    }
                }
            }
            if (fieldMethod != null) {
                Object o;
                try {
                    fieldMethod.setAccessible(true);
                    o = fieldMethod.invoke(bean);
                } catch (Exception e) {
                    return null;
                }
                if (o == null) {
                    return null;
                }
                //Obter o objeto se não houver mais membros.
                if (fieldNames.length == 1) {
                    return o;
                } else {
                    return getBeanAttributeValue(name.substring(name.indexOf(".") + 1), o);
                }
            }
        }
        //Erro
        return null;
    }

    @Override
    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
        set.add(new NodeRenderingHandler<>(Bean.class, new CustomNodeRenderer<Bean>() {
            @Override
            public void render(Bean node, NodeRendererContext context, HtmlWriter html) {
                BeanNodeRenderer.this.render(node, context, html);
            }
        }));

        return set;
    }

    private void render(Bean node, NodeRendererContext context, HtmlWriter html) {
        if (mMarkdownView != null && mMarkdownView.getBean() != null) {
            String name = node.getText().toString().trim();
            Object value = getBeanAttributeValue(name, mMarkdownView.getBean());
            if (value != null) {
                html.attr("class", "bean");
                html.withAttr().tag("span");
                html.append(value.toString());
                html.tag("/span");
            }
        } else {
            context.renderChildren(node);
        }
    }

    public static class Factory implements NodeRendererFactory {
        @Override
        public NodeRenderer create(final DataHolder options) {
            return new BeanNodeRenderer(options);
        }
    }
}
