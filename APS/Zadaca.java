import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.*;


class SLLNode<E> {
    protected E element;
    protected SLLNode<E> succ;

    public SLLNode(E elem, SLLNode<E> succ) {
        this.element = elem;
        this.succ = succ;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}


class MapEntry<K extends Comparable<K>,E> implements Comparable<K> {

    // Each MapEntry object is a pair consisting of a key (a Comparable
    // object) and a value (an arbitrary object).
    K key;
    E value;

    public MapEntry (K key, E val) {
        this.key = key;
        this.value = val;
    }

    public int compareTo (K that) {
        // Compare this map entry to that map entry.
        @SuppressWarnings("unchecked")
        MapEntry<K,E> other = (MapEntry<K,E>) that;
        return this.key.compareTo(other.key);
    }

    public String toString () {
        return "<" + key + "," + value + ">";
    }
}

class CBHT<K extends Comparable<K>, E> {

    // An object of class CBHT is a closed-bucket hash table, containing
    // entries of class MapEntry.
    private SLLNode<MapEntry<K,E>>[] buckets;

    @SuppressWarnings("unchecked")
    public CBHT(int m) {
        // Construct an empty CBHT with m buckets.
        buckets = (SLLNode<MapEntry<K,E>>[]) new SLLNode[m];
    }

    private int hash(K key) {
        // Translate key to an index of the array buckets.
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public SLLNode<MapEntry<K,E>> search(K targetKey) {
        // Find which if any node of this CBHT contains an entry whose key is
        // equal
        // to targetKey. Return a link to that node (or null if there is none).
        int b = hash(targetKey);
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (targetKey.equals(((MapEntry<K, E>) curr.element).key))
                return curr;
        }
        return null;
    }

    public void insert(K key, E val) {        // Insert the entry <key, val> into this CBHT.
        MapEntry<K, E> newEntry = new MapEntry<K, E>(key, val);
        int b = hash(key);//karposh
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (key.equals(((MapEntry<K, E>) curr.element).key)) {
                // Make newEntry replace the existing entry ...
                curr.element = newEntry;
                return;
            }
        }

//OVA GORE IF SE KOMENTIRA CELO ZA DA NE VNESUVA NA SAMOTO MESTO KADE SHTO E TUKU DA GO VNESE KAKO DUPLIKAT DA IMA VO SLUCAJOT 2 KLUCA NE 1.


        // Insert newEntry at the front of the 1WLL in bucket b ...
        buckets[b] = new SLLNode<MapEntry<K,E>>(newEntry, buckets[b]);
    }

    public void delete(K key) {
        // Delete the entry (if any) whose key is equal to key from this CBHT.
        int b = hash(key);
        for (SLLNode<MapEntry<K,E>> pred = null, curr = buckets[b]; curr != null; pred = curr, curr = curr.succ) {
            if (key.equals(((MapEntry<K,E>) curr.element).key)) {
                if (pred == null)
                    buckets[b] = curr.succ;
                else
                    pred.succ = curr.succ;
                return;
            }
        }
    }

    public String toString() {
        String temp = "";
        for (int i = 0; i < buckets.length; i++) {
            temp += i + ":";
            for (SLLNode<MapEntry<K,E>> curr = buckets[i]; curr != null; curr = curr.succ) {
                temp += curr.element.toString() + " ";
            }
            temp += "\n";
        }
        return temp;
    }

}
class Pacient {
    String prezime;
    boolean zarazen;

    public Pacient(String prezime, boolean zarazen) {
        this.prezime = prezime;
        this.zarazen = zarazen;
    }
}

public class Zadaca {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        CBHT<String, ArrayList<Pacient>> hashtable = new CBHT<>(2*n);
        for(int i = 0; i < n; i++){
            String []read = br.readLine().split("\\s");
            String opstina = read[0];
            String prezime = read[1];
            boolean zarazen;
            if(read[2].equals("negative")){
                zarazen=false;
            } else zarazen=true;
            Pacient pacient = new Pacient(prezime,zarazen);
            if(hashtable.search(opstina)==null){
                ArrayList<Pacient> tmp = new ArrayList<>();
                tmp.add(pacient);
                hashtable.insert(opstina,tmp);
            } else {
                ArrayList<Pacient> tmp = hashtable.search(opstina).element.value;
                tmp.add(pacient);
                hashtable.insert(opstina,tmp);
            }
        }
        String opstina = br.readLine();
        double riskFactor = 0;
        SLLNode<MapEntry<String,ArrayList<Pacient>>> result = hashtable.search(opstina);
        ArrayList<Pacient> tmp = result.element.value;
        double zarazeni = 0;
        double zdravi = 0;
        for (Pacient pacient : tmp) {
            if(pacient.zarazen==true){
                zarazeni++;
            } else zdravi++;
        }
        riskFactor=(zarazeni)/(zdravi+zarazeni);
        System.out.println(riskFactor);


    }
}
