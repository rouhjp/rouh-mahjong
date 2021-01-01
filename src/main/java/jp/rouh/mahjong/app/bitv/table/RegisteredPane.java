package jp.rouh.mahjong.app.bitv.table;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class RegisteredPane extends JLayeredPane{
    private final Map<String, Component> components = new HashMap<>();

    /**
     * 名前付きでコンポーネントを追加します。
     *
     * <p>既に同名のコンポーネントが登録されている場合は
     * そのコンポーネントを削除した後追加を行います。
     * @param component 追加するコンポーネント
     * @param name 名前
     */
    public void addNamed(Component component, String name){
        if(components.containsKey(name)){
            removeByName(name);
        }
        components.put(name, component);
        add(component);
    }

    /**
     * 指定した名前で登録されたコンポーネントを削除します。
     * @throws NoSuchElementException コンポーネントが見つからない場合
     * @param name 名前
     */
    public void removeByName(String name){
        if(!components.containsKey(name)){
            throw new NoSuchElementException();
        }
        remove(components.get(name));
        components.remove(name);
    }

//    /**
//     * 指定した名前でコンポーネントが登録されているか検査します。
//     * @param name 名前
//     * @return true  登録されている場合
//     *         false 登録されていない場合
//     */
//    public boolean contains(String name){
//        return components.containsKey(name);
//    }

    /**
     * 指定した名前でコンポーネントが登録されている場合, そのコンポーネントを削除します。
     * @param name 名前
     */
    public void removeByNameIfPresent(String name){
        if(components.containsKey(name)){
            remove(components.get(name));
            components.remove(name);
        }
    }

    /**
     * {@inheritDoc}
     * 名前とコンポーネントの関連を全て削除します。
     */
    @Override
    public void removeAll(){
        super.removeAll();
        components.clear();
    }

    /**
     * 述語関数に適合する名前と関連付けられたコンポーネントを全て削除します。
     * @param namePredicate 述語関数
     */
    public void removeAll(Predicate<? super String> namePredicate){
        for(var name:components.keySet()){
            if(namePredicate.test(name)){
                removeByName(name);
            }
        }
    }

}
