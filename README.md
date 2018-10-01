AutoSave [![CircleCI branch](https://img.shields.io/circleci/project/github/RedSparr0w/node-csgo-parser/master.svg?style=flat-square)](https://circleci.com/gh/SleepyTrousers/AutoSave)
===========

Automatically save, load, and sync NBT data with a easy to use, easy to extend, annotation based API.

## How To Use

### Add as a dependency

First add tterrag's alternate maven to your buildscript
```groovy
repositories {
    maven { url = "http://maven2.tterrag.com" } // AutoSave
}
```

Then add AutoSave as a maven dependency
```groovy
dependencies {
    compile "info.loenwind.autosave:AutoSave:${minecraft_version}-${autosave_version}"
}
```
Where `autosave_version` is defined in your `gradle.properties` file. See the [maven listings](https://maven2.tterrag.com/info/loenwind/autosave/AutoSave/) for versions.

However, to allow for multiple mods to depend on AutoSave without conflict (and without shading) we recommend you use Forge's [Contained Dependencies](https://mcforge.readthedocs.io/en/latest/gettingstarted/dependencymanagement/) system.

### Label your fields

Simply annotate any fields that need to be saved with `@Store`.

```java
public class MyObject {
    
    @Store
    private String name;
    
    @Store
    private int counter;
}
```

### Serialize With Ease

The (de)serialization API is very simple, only a single line of code!

#### To write an object to NBT:

```java
NBTTagCompound tag = new NBTTagCompound();
Writer.write(tag, object);
```

#### To read an object from NBT:

```java
Reader.read(tag, object);
```

### Automatic recursive serializer support

```java
@Storable
public class StorableExample {
    
    @Store
    private String name;
}

public class Wrapper {
    
    @Store
    private StorableExample storable;
}

Writer.write(tag, new Wrapper()); // This will work!
```

### Extend the supported field types

```java
Registry registry = new Registry();
registry.register(new HandleMyObject());

...

class Wrapper {

    @Store
    private MyObject myObject = new MyObject();
}

NBTTagCompound tag = new NBTTagCompound();
Writer.write(registry, tag, new Wrapper());
```

### Out-of-the-box Supported Types

#### Special Cases

- Array types will automatically look for a handler for their component type
- Supported generic types will do the same (List, Map, etc. See [Java](#java))

#### Java

- All primitive types (including boxed types)
- String
- Enum
- List (ArrayList)
- LinkedList
- Set (HashSet)
- Map (HashMap)
- EnumMap
- Enum2EnumMap (Not a class, but a special case of EnumMap where the data is packed more efficiently)

#### Minecraft

- BlockPos
- Block
- IBlockState
- Item
- ItemStack
- ResourceLocation

#### Forge

- Fluid
- FluidStack

<h5>For more information, refer to the javadocs.</h5>
