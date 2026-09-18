package com.tracker.util;

import com.tracker.model.Skill;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Custom algorithms utility class providing linear search, binary search, and sorting.
 * Demonstrates DSA / Algorithm concepts in Java.
 */
public class AlgorithmUtil {

    /**
     * Performs Case-Insensitive Linear Search on a list of Skills by name.
     */
    public static Skill linearSearchByName(List<Skill> skills, String targetName) {
        if (skills == null || targetName == null) return null;
        for (Skill s : skills) {
            if (s.getName().equalsIgnoreCase(targetName.trim())) {
                return s;
            }
        }
        return null;
    }

    /**
     * Performs Binary Search on a sorted list of Skills by name.
     * Note: List MUST be sorted alphabetically by name prior to invoking binary search.
     */
    public static Skill binarySearchByName(List<Skill> sortedSkills, String targetName) {
        if (sortedSkills == null || targetName == null || sortedSkills.isEmpty()) return null;

        int low = 0;
        int high = sortedSkills.size() - 1;
        String target = targetName.trim().toLowerCase();

        while (low <= high) {
            int mid = low + (high - low) / 2;
            Skill midSkill = sortedSkills.get(mid);
            int cmp = midSkill.getName().toLowerCase().compareTo(target);

            if (cmp == 0) {
                return midSkill;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    /**
     * Custom MergeSort implementation for sorting Skills by Proficiency Weight (Descending).
     * Demonstrates recursive divide-and-conquer algorithm implementation in Java.
     */
    public static List<Skill> sortByProficiencyDescending(List<Skill> list) {
        if (list == null || list.size() <= 1) return new ArrayList<>(list != null ? list : List.of());
        List<Skill> sorted = new ArrayList<>(list);
        mergeSortProficiency(sorted, 0, sorted.size() - 1);
        return sorted;
    }

    private static void mergeSortProficiency(List<Skill> list, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortProficiency(list, left, mid);
            mergeSortProficiency(list, mid + 1, right);
            mergeProficiency(list, left, mid, right);
        }
    }

    private static void mergeProficiency(List<Skill> list, int left, int mid, int right) {
        List<Skill> temp = new ArrayList<>();
        int i = left, j = mid + 1;

        while (i <= mid && j <= right) {
            if (list.get(i).getProficiencyWeight() >= list.get(j).getProficiencyWeight()) {
                temp.add(list.get(i++));
            } else {
                temp.add(list.get(j++));
            }
        }

        while (i <= mid) temp.add(list.get(i++));
        while (j <= right) temp.add(list.get(j++));

        for (int k = 0; k < temp.size(); k++) {
            list.set(left + k, temp.get(k));
        }
    }
}
