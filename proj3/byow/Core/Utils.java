package byow.Core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.HashSet;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File SAVED_FILE = join(CWD, "savedfile.txt");

    /** Return the entire contents of FILE as a byte array.  FILE must
     *  be a normal file.  Throws IllegalArgumentException
     *  in case of problems. */
    static byte[] readContents(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("must be a normal file");
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /** Return the entire contents of FILE as a String.  FILE must
     *  be a normal file.  Throws IllegalArgumentException
     *  in case of problems. */
    static String readContentsAsString(File file) {
        return new String(readContents(file), StandardCharsets.UTF_8);
    }

    /** Write the result of concatenating the bytes in CONTENTS to FILE,
     *  creating or overwriting it as needed.  Each object in CONTENTS may be
     *  either a String or a byte array.  Throws IllegalArgumentException
     *  in case of problems. */
    static void writeContents(File file, Object... contents) {
        try {
            if (file.isDirectory()) {
                throw new IllegalArgumentException("cannot overwrite directory");
            }
            BufferedOutputStream str =
                    new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            for (Object obj : contents) {
                if (obj instanceof byte[]) {
                    str.write((byte[]) obj);
                } else {
                    str.write(((String) obj).getBytes(StandardCharsets.UTF_8));
                }
            }
            str.close();
        } catch (IOException | ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /** Return an object of type T read from FILE, casting it to EXPECTEDCLASS.
     *  Throws IllegalArgumentException in case of problems. */
    static <T extends Serializable> T readObject(File file,
                                                 Class<T> expectedClass) {
        try {
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(file));
            T result = expectedClass.cast(in.readObject());
            in.close();
            return result;
        } catch (IOException | ClassCastException
                 | ClassNotFoundException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /** Write OBJ to FILE. */
    static void writeObject(File file, Serializable obj) {
        writeContents(file, serialize(obj));
    }

    /* OTHER FILE UTILITIES */

    /** Return the concatentation of FIRST and OTHERS into a File designator,
     *  analogous to the {
     *      @link java.nio.file.Paths.#get(String, String[])
     *  } method. */
    static File join(String first, String... others) {
        return Paths.get(first, others).toFile();
    }


        /** Return the concatentation of FIRST and OTHERS into a File designator,
         *  analogous to the {
         *      @link java.nio.file.Paths.#get(String, String[])
         *  } method. */
    static File join(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }


    /* SERIALIZATION UTILITIES */

    /** Returns a byte array containing the serialized contents of OBJ. */
    static byte[] serialize(Serializable obj) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(obj);
            objectStream.close();
            return stream.toByteArray();
        } catch (IOException excp) {
            throw new RuntimeException("Internal error serializing");
        }
    }


    /* ENGINE UTILS */

    /* Add more keys if necessary */
    public static final HashSet<Character> NUMBER_SET = Stream.of(
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
    ).collect(Collectors.toCollection(HashSet::new));

    public static final HashSet<Character> SYMBOL_SET = Stream.of(
            'a', 's', 'w', 'd', 'l', 'q', ':', 'n'
    ).collect(Collectors.toCollection(HashSet::new));

    public static String parseCommand(String input, int startIndex) {
        String actions = input.substring(startIndex);
        int colonIndex = input.indexOf(':');
        if (colonIndex != -1) {
            actions = input.substring(startIndex, colonIndex);
        }

        return actions;
    }

    /** We need to ensure the least significant character first */
    public static long getSeedFromString(String seedString) {
        return Long.parseLong(seedString, 10);
    }

    public static void usageHelper() {
        System.out.println("Input string should be valid! (case insensitive)");
        System.out.println("Usage: command [action]");
        System.out.println("\tN3412S (create a new world with seed 3412)");
        System.out.println("\tN123SSS:q (create a new world with seed 123, " +
                "move down twice then quit and save)");
    }

    public static Property load() {
        System.out.println("Load previous game world...");
        return readObject(SAVED_FILE, Property.class);
    }

    public static void save(Property property) {
        writeObject(SAVED_FILE, property);
    }

    public static void quit(Property property) {
        System.out.println("Quit game...");
        save(property);
    }

    public static void move(String actions, Property property) {
        if (actions == null || actions.isEmpty() || actions.contains(":")) {
            return;
        }

        for (char a : actions.toCharArray()) {
            // DONE: Add logic to realize movement function of avatar
            property.gameWorld = property.shadowWorld.clone();
            property.avatar.avatarMove(property.gameWorld, Character.toString(a));
        }
    }

    /* Kruskal UNION */

    public static boolean isConnected(Point p1, Point p2, HashMap<Point, Point> root) {
        return kruskalFind(p1, root).equals(kruskalFind(p2, root));
    }

    /** find ancestor */
    public static Point kruskalFind(Point unit, HashMap<Point, Point> root) {
        if (root.get(unit) != unit) {
            root.put(unit, kruskalFind(root.get(unit), root));
        }
        return root.get(unit);
    }

    /** We use rank union method, which will reduce union tree depth */
    public static void kruskalUnion(Point unit1, Point unit2, HashMap<Point, Point> root) {
        Point root1 = kruskalFind(unit1, root);
        Point root2 = kruskalFind(unit2, root);
        // Attach the lower rank to higher rank
        if (root1.getRank() <= root2.getRank()) {
            root.put(root1, root2);
        } else {
            root.put(root2, root1);
        }
        if (root1.getRank() == root2.getRank() && !root1.equals(root2)) {
            root2.addRank();
        }
    }

}
