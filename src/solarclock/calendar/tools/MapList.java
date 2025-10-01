package solarclock.calendar.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author sebastien
 * @param <U>
 * @param <T>
 */
public class MapList<U, T> extends HashMap<U, List<T>> {

    public void add(U key, T value) {
        List<T> lst = get(key);
        if (lst == null) {
            lst = new ArrayList<>();
            put(key, lst);
        }
        lst.add(value);
    }

    /**
     * Ajout de plusieurs valeurs dans la liste key
     *
     * @param key
     * @param values
     */
    public void add(U key, Collection<T> values) {
        List<T> lst = get(key);
        if (lst == null) {
            lst = new ArrayList<>();
            put(key, lst);
        }
        lst.addAll(values);
    }

    public void add(Collection<U> keys, T value) {
        keys.forEach((k) -> add(k, value));
    }

    public void add(U key, T... values) {
        add(key, Arrays.asList(values));
    }

    public void addAll(final MapList<U, T> keyValues) {
        for (Entry<U, List<T>> e : keyValues.entrySet()) {
            add(e.getKey(), e.getValue());
        }
    }

    public boolean removeInList(U key, T value) {
        List<T> lst = get(key);
        if (lst != null) {
            boolean result = lst.remove(value);
            if (lst.isEmpty()) {
                remove(key, lst); // On supprime les listes vide (voir si c est un bon choix)
            }
            return result;
        }
        return false;
    }
    
    public List<T> getList(U key) {
        List<T> lst = get(key);
        return lst == null ? new ArrayList<>() : lst;
    }

    public MapList<T, U> getInvert() {
        final MapList<T, U> inv = new MapList<>();
        for (Entry<U, List<T>> e : entrySet()) {
            for (T o : e.getValue()) {
                inv.add(o, e.getKey());
            }
        }
        return inv;
    }

    public boolean contains(U key, T name) {
        final List<T> lst = get(key);
        return lst != null && lst.contains(name);
    }

    /**
     * Permet d'iterer sur toutes les valeurs sans creer de liste intermediaire
     *
     * @return
     */
    public Iterable<T> iterAllValues() {
        return () -> new Iterator<T>() {
            final Iterator<U> itKey = keySet().iterator();
            Iterator<T> itLst = nextList();

            @Override
            public boolean hasNext() {
                return itLst != null && itLst.hasNext();
            }
            @Override
            public T next() {
                T val = itLst.next();
                itLst = nextList(); // changement de liste si besoin
                return val;
            }
            public Iterator<T> nextList() {
                if (itLst != null && itLst.hasNext()) { 
                    return itLst; // on continu sur cette liste
                }
                while (itKey.hasNext()) {
                    Iterator<T> itLst0 = get(itKey.next()).iterator();
                    if (itLst0.hasNext()) {// on zap les listes vides
                        return itLst0;
                    }
                }
                return null;
            }
        };
    }

    public List<T> allValues() {
        List<T> all = new ArrayList<>();
        values().forEach((lst) -> {
            if (lst != null) {
                all.addAll(lst);
            }
        });
        return all;
    }

    public int sizeAll() {
        int sz = 0;
        for (List<T> lst : values()) {
            if (lst != null) {
                sz += lst.size();
            }
        }
        return sz;
    }
}
