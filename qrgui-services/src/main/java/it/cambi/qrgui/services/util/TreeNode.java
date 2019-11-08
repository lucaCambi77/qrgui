package it.cambi.qrgui.services.util;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<C, P>
{

    private P data;
    private C root;
    private List<TreeNode<C, P>> childrens;

    public TreeNode()
    {
        // TODO Auto-generated constructor stub
    }

    public TreeNode(C root, P data)
    {
        this.root = root;
        this.data = data;
        this.childrens = new ArrayList<>();
    }

    public TreeNode<C, P> addChild(P rootId, C child)
    {
        TreeNode<C, P> children = new TreeNode<C, P>(child, rootId);
        this.childrens.add(children);

        return children;
    }

    public P getData()
    {
        return data;
    }

    public List<TreeNode<C, P>> getChildrens()
    {
        return childrens;
    }

    public C getRoot()
    {
        return root;
    }

}
