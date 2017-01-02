package com.orctom.gradle.archetype

/**
 *  Strategies for resolving conflicts.
 *  Used for decision-making during project generation namely during writing of the output.
 */
enum ConflictResolutionStrategy {

    /** Target directory is deleted and re-created before project generation starts. */
    SWEEP,

    /** Existing files are rewritten, existing directories ignored. */
    OVERWRITE,

    // TODO, only for interactive work
    /**
     * User is asked if the existing file should be rewritten or not - yes/no/merge/abort.
     * Merge is provided only when a merge tool is set via system property mergeTool.
     */
    //ASK,

    /**
     * Generation stops it when it encounters an existing file,
     * already generated files are removed.
     */
    FAIL;
}