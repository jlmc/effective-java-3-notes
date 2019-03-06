# Java's serialization mechanism

Java's serialization mechanism is very powerful and flexible.
When an instance gets serialized, ObjectOutputStream looks for a handful of "magic methods" that the developer can provide to customize the process. In this short article, we'll see what they are, what they do and in which order they are called in the serialization pipeline.

## During serialization

**- writeObject**

```
private void writeObject (ObjectOutputStream out) throws IOException
```

This method allows to take complete control over what will be sent over the wire.
In most cases, you will just call `out.defaultWriteObject()` to benefit from the default serialization process, then add some more data of your choice (for instance, data from the parent class) by calling  `out.writeDouble`, `out.writeUTF`, etc. (inherited by `ObjectOutputStream` from the DataOutput interface).

**- writeReplace**

```
private Object writeReplace() throws ObjectStreamException
```
This method allows the developer to provide a replacement object that will be serialized instead of the original one.



## During de-serialization

**- readObject**

```
private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
```

This method is the same as writeObject above, but for reading objects from the serialized stream.
You can call `in.defaultReadObject()` to automatically read most of the data, then manually read back the extra data you may have added. Be careful to read data in the same exact order they were written in the stream !

This method is also where you can declare stream validators.

**- validateObject**

```
public void validateObject() throws InvalidObjectException

```

If the serialized object implements ObjectInputValidation, you may register it as a stream validator.
Useful to verify the stream has not been tampered with, or that the data makes sense before handing it back to your application.


**- readResolve**

```
private Object readResolve() throws ObjectStreamException
```

This method mirrors `writeReplace` : it may be used to replace the de-serialized object by another one of your choice.



### The serialization / de-serialization pipeline
Now that you know what magic methods exist and their typical use, let's see in what order they are called during a serialization / de-serialization roundtrip.

```

public class Pojo implements Serializable, ObjectInputValidation {
 
    private String msg;
 
    public Pojo(String msg) {
        this.msg = msg;
    }
 
    public String getMsg() {
        return msg;
    }
 
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        System.out.println("writeObject");
        out.defaultWriteObject();
    }
 
    private Object writeReplace() throws ObjectStreamException {
        System.out.println("writeReplace");
        return this;
    }
 
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        System.out.println("readObject");
        in.registerValidation(this, 0);
        in.defaultReadObject();
    }
 
    @Override
    public void validateObject() throws InvalidObjectException {
        System.out.println("validateObject");
    }
 
    private Object readResolve() throws ObjectStreamException {
        System.out.println("readResolve");
        return this;
    }
}

```


```
public class Test {
 
    public static void main(String[] args) throws Exception {
        Pojo pojo = new Pojo("Hello world");
        byte[] bytes = serialize(pojo); // Serialization        
        Pojo p = (Pojo) deserialize(bytes); // De-serialization
        System.out.println(p.getMsg());
    }
 
    private static byte[] serialize(Object o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.flush();
        oos.close();
        return baos.toByteArray();
    }
 
    private static Object deserialize(byte[] bytes) throws ClassNotFoundException, IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        ois.close();
        return o;
    }
 
}

```

When this test program is executed, it prints the following :

```
writeReplace
writeObject
readObject
readResolve
validateObject
Hello world
```

### Interesting facts :

- The writeReplace method is executed first. 
- The rest of the serialization process will be applied to the replacement object, if any.

- As expected, the validation method is executed on the replacement object, not on the one that was originally de-serialized - this one will be silently discarded.

