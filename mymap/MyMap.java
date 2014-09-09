package mymap;

import java.util.Vector;

public class MyMap<K,V> {

private Vector<Entry> entries;

public MyMap(K[] keyArray, V[] valueArray) throws UnsupportedOperationException
{
	entries=new Vector<Entry>();
	
	if(keyArray.length!=valueArray.length)
		throw new UnsupportedOperationException();
	
	for(int i=0;i<keyArray.length;i++){
		entries.add(new Entry(keyArray[i], valueArray[i]));
	}
}

public MyMap(K[] keyArray)
{
	entries=new Vector<Entry>();
	
	for(K k:keyArray)
		entries.add(new Entry(k, null));
}

public class Entry{
	K key;
	V value;
	protected Entry(K key, V value){
		this.key=key;
		this.value=value;
	}
	
	public String toString(){		
		return key.toString()+" : "+((value==null)?"null":value.toString());
	}
	
	public V value(){
		return value;
	}
}


public V get(K key) throws NoSuchFieldException
{
	for(Entry e: entries){
		if(e.key.equals(key))
			return e.value;
	}
	throw new NoSuchFieldException();
}

public K getKeyAt(int index)
{
	return entries.get(index).key;
}

public V getValueAt(int index){
	return entries.get(index).value;
}

public void put(K key, V value)
{
	if(key==null)
		throw new NullPointerException();
	int index=indexOf(key);
	
	if(index!=-1){
		entries.get(index).value=value;
	}
	else{
	entries.add(new Entry(key, value));
	}
}

public String toString(){
	StringBuffer sb=new StringBuffer();
	
	for(Entry en:entries){
		sb.append(en+"\n");
	}
	
	return sb.toString();
}

public boolean contains(K key){
	return (indexOf(key)==-1)?false:true;
}

public int indexOf(K key){
	for(int i=0;i<entries.size();i++){
		if(entries.get(i).key.equals(key))
			return i;
	}
	return -1;
}
public int size(){
	return entries.size();
}

public Entry getEntry(int index){
	return entries.get(index);
}

}
