package scm.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static scm.address.testutil.TypicalPersons.getTypicalAddressBook;
import static scm.address.testutil.TypicalSchedules.getTypicalScheduleList;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import scm.address.commons.core.GuiSettings;
import scm.address.model.AddressBook;
import scm.address.model.ReadOnlyAddressBook;
import scm.address.model.ReadOnlyScheduleList;
import scm.address.model.ScheduleList;
import scm.address.model.UserPrefs;
import scm.address.model.theme.ThemeCollection;

public class StorageManagerTest {

    private static final String DARK_THEME = ThemeCollection.getDarkTheme().getThemeName();
    @TempDir
    public Path testFolder;
    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        JsonScheduleStorage jsonScheduleStorage = new JsonScheduleStorage(getTempFilePath("schedules"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage, jsonScheduleStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6, DARK_THEME));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonAddressBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonAddressBookStorageTest} class.
         */
        AddressBook original = getTypicalAddressBook();
        storageManager.saveAddressBook(original);
        ReadOnlyAddressBook retrieved = storageManager.readAddressBook().get();
        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }

    @Test
    public void getScheduleListFilePath() {
        assertNotNull(storageManager.getScheduleStorageFilePath());
    }

    @Test
    public void scheduleListReadSave() throws Exception {
        ScheduleList original = getTypicalScheduleList();
        storageManager.saveScheduleList(original);
        ReadOnlyScheduleList retrieved = storageManager.readScheduleList().get();
        assertEquals(original, new ScheduleList(retrieved));
    }

}
