package de.jthedroid.whatsappchatanalyzer.bintree;

import java.util.ArrayList;

public class BinTree<T extends Sortable> {

    private final T content;
    private BinTree<T> left, right;

    public BinTree(T content) {
        this.content = content;
    }

    public ArrayList<T> sort() {
        ArrayList<T> list = new ArrayList<>();
        sort(list);
        return list;
    }

    private void sort(ArrayList<T> list) {
        if (left != null) {
            left.sort(list);
        }
        list.add(content);
        if (right != null) {
            right.sort(list);
        }
    }

    public void addContent(T newContent) {
        if (newContent.getNum() < content.getNum()) {
            if (left == null) {
                left = new BinTree<>(newContent);
            } else {
                left.addContent(newContent);
            }
        } else {
            if (right == null) {
                right = new BinTree<>(newContent);
            } else {
                right.addContent(newContent);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GetterSetter">
    public T getContent() {
        return content;
    }

    public BinTree<T> getLeft() {
        return left;
    }

    public void setLeft(BinTree<T> left) {
        this.left = left;
    }

    public BinTree<T> getRight() {
        return right;
    }

    public void setRight(BinTree<T> right) {
        this.right = right;
    }
//</editor-fold>

}
