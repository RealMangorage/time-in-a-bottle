package org.mangorage.tiab.common.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.lang.Styles;
import org.mangorage.tiab.common.lang.Translation;

import java.util.function.Function;
import java.util.function.Supplier;

public class CommonHelper {
    private static final String[] NOTES = {"C", "D", "E", "F", "G2", "A2", "B2", "C2", "D2", "E2", "F2"};

    public static void playNoteBlockHarpSound(Level level, BlockPos pos, String note) {
        // https://minecraft.gamepedia.com/Note_Block
        switch (note) {
            // Octave 1
            case "F#" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.5F);
            case "G" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.529732F);
            case "G#" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.561231F);
            case "A" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.594604F);
            case "A#" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.629961F);
            case "B" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.667420F);
            case "C" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.707107F);
            case "C#" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.749154F);
            case "D" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.793701F);
            case "D#" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.840896F);
            case "E" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.890899F);
            case "F" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 0.943874F);

            // Octave 2
            case "F#2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1F);
            case "G2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1.059463F);
            case "G#2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1.122462F);
            case "A2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1.189207F);
            case "A#2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1.259921F);
            case "B2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1.334840F);
            case "C2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1.414214F);
            case "C#2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1.498307F);
            case "D2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1.587401F);
            case "D#2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1.681793F);
            case "E2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1.781797F);
            case "F2" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 1.887749F);
            case "F#3" -> level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.BLOCKS, 0.5F, 2F);
        }
    }

    public static void playSound(Level level, BlockPos pos, int nextRate) {
        switch (nextRate) {
            case 1 -> playNoteBlockHarpSound(level, pos, NOTES[0]);
            case 2 -> playNoteBlockHarpSound(level, pos, NOTES[1]);
            case 4 -> playNoteBlockHarpSound(level, pos, NOTES[2]);
            case 8 -> playNoteBlockHarpSound(level, pos, NOTES[3]);
            case 16 -> playNoteBlockHarpSound(level, pos, NOTES[4]);
            case 32 -> playNoteBlockHarpSound(level, pos, NOTES[5]);
            case 64 -> playNoteBlockHarpSound(level, pos, NOTES[6]);
            case 128 -> playNoteBlockHarpSound(level, pos, NOTES[7]);
            case 256 -> playNoteBlockHarpSound(level, pos, NOTES[8]);
            case 512 -> playNoteBlockHarpSound(level, pos, NOTES[9]);
            default -> playNoteBlockHarpSound(level, pos, NOTES[10]);
        }
    }

    public static Component getTotalTimeTranslated(ItemStack stack) {
        int totalAccumulatedTime = stack.getOrDefault(CommonRegistration.STORED_TIME_COMPONENT.get(), new StoredTimeComponent(0, 0)).total();
        int totalAccumulatedTimeSeconds = totalAccumulatedTime / 20;
        int totalAccumulatedHours = totalAccumulatedTimeSeconds / 3600;
        int totalAccumulatedMinutes = (totalAccumulatedTimeSeconds % 3600) / 60;
        int totalAccumulatedSeconds = totalAccumulatedTimeSeconds % 60;

        return Translation.TOOLTIP_TOTAL_ACCUMULATED_TIME.componentTranslation(String.format("%02d", totalAccumulatedHours), String.format("%02d", totalAccumulatedMinutes), String.format("%02d", totalAccumulatedSeconds)).setStyle(Styles.GRAY);
    }

    public static Component getStoredTimeTranslated(ItemStack stack) {
        int storedTime = stack.getOrDefault(CommonRegistration.STORED_TIME_COMPONENT.get(), new StoredTimeComponent(0, 0)).stored();
        int storedSeconds = storedTime / 20;
        int hours = storedSeconds / 3600;
        int minutes = (storedSeconds % 3600) / 60;
        int seconds = storedSeconds % 60;

        return Translation.TOOLTIP_STORED_TIME.componentTranslation(String.format("%02d", hours), String.format("%02d", minutes), String.format("%02d", seconds)).setStyle(Styles.GREEN);
    }

    public static <T> void modify(ItemStack stack, DataComponentType<T> dataComponentType, Supplier<T> defaultComponent, Function<T, T> function) {
        var comp = stack.getOrDefault(dataComponentType, defaultComponent.get());
        stack.set(dataComponentType, function.apply(comp));
    }

    public static boolean isPositive(int number) {
        return !(number < 0);
    }
}
