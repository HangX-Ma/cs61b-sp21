package byow.Core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File saveFile = join(CWD, "saved.txt");

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
                throw
                        new IllegalArgumentException("cannot overwrite directory");
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

    // TODO: Add more keys if necessary
    public static final HashSet<Character> numberSet = Stream.of(
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
    ).collect(Collectors.toCollection(HashSet::new));

    public static final HashSet<Character> symbolSet = Stream.of(
            'a', 's', 'w', 'd', 'l', 'q', ':', 'n'
    ).collect(Collectors.toCollection(HashSet::new));



    /** The input format will be 'N####S' + Action letters.
     *  We will deal with the string after 'N' and attach 'q' at front if found.
     */
    public static Pair<Long, List<Character>> parseCommandN(String input) {
        long seed;
        List<Character> actionList = new LinkedList<>();

        int letterIndex = input.indexOf('s');; // first 's' index
        int colonIndex = input.indexOf(':'); // first ':' index

        /* Covert seed string to long type */
        String seedString = input.substring(0, letterIndex);
        seed = getSeedFromString(seedString);

        /* obtain actions list */
        if (colonIndex != -1) {
            /* attach the action after the colon and discard all others */
            actionList.add(input.charAt(colonIndex + 1));

            String actions = input.substring(letterIndex + 1, colonIndex);
            catenateActions(actionList, actions);
        } else {
            /* No colon found but actions */
            if (letterIndex < input.length() - 1) {
                String actions = input.substring(letterIndex + 1);
                catenateActions(actionList, actions);
            }
        }

        return new Pair<>(seed, actionList);
    }

    public static Pair<Long, List<Character>> parseCommandL(String input) {
        String actions = input;
        List<Character> actionList = new LinkedList<>();
        int colonIndex = input.indexOf(':');
        if (colonIndex != -1) {
            actions = input.substring(0, colonIndex);
            actionList.add(input.charAt(colonIndex + 1));
        }
        catenateActions(actionList, actions);

        return new Pair<>(null, actionList);
    }

    /** Put actions in action list */
    private static void catenateActions(List<Character> actionList, String actions) {
        for (char action : actions.toCharArray()) {
            actionList.add(action);
        }
    }

    /** We need to ensure the least significant character first */
    private static long getSeedFromString(String seedString) {
        return Long.parseLong(seedString, 10);
    }

    public static void usageHelper() {
        System.out.println("Input string should be valid! (case insensitive)");
        System.out.println("Usage: command [action]");
        System.out.println("\tN3412S (create a new world with seed 3412)");
        System.out.println("\tN123SSS:q (create a new world with seed 123, move down twice then quit and save)");
    }

    public static Property load() {
        return readObject(saveFile, Property.class);
    }

    public static void save(Property obj) {
        writeObject(saveFile, obj);
    }

    public static void quit(Property obj) {
        save(obj);
    }

}
