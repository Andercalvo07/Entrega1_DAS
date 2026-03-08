private fun showAddWorkoutDialog() {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_workout, null)
    val titleInput = dialogView.findViewById<TextInputEditText>(R.id.workout_title)
    val descriptionInput = dialogView.findViewById<TextInputEditText>(R.id.workout_description)
    val typeSpinner = dialogView.findViewById<Spinner>(R.id.workout_type_spinner)
    val daySpinner = dialogView.findViewById<Spinner>(R.id.day_spinner)
    val hourSpinner = dialogView.findViewById<Spinner>(R.id.hour_spinner)
    val durationInput = dialogView.findViewById<TextInputEditText>(R.id.workout_duration)
    val saveButton = dialogView.findViewById<Button>(R.id.save_button)
    val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)

    // Setup type spinner
    val types = resources.getStringArray(R.array.workout_types)
    val typeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
    typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    typeSpinner.adapter = typeAdapter

    // Setup day spinner
    val days = resources.getStringArray(R.array.days_of_week)
    val dayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, days)
    dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    daySpinner.adapter = dayAdapter

    // Setup hour spinner
    val hours = (0..23).map { String.format("%02d:00", it) }
    val hourAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hours)
    hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    hourSpinner.adapter = hourAdapter

    val dialog = MaterialAlertDialogBuilder(requireContext())
        .setView(dialogView)
        .create()

    saveButton.setOnClickListener {
        val title = titleInput.text.toString()
        val description = descriptionInput.text.toString()
        val type = typeSpinner.selectedItem.toString()
        val day = daySpinner.selectedItem.toString()
        val hour = hourSpinner.selectedItem.toString()
        val duration = durationInput.text.toString().toIntOrNull() ?: 0

        if (title.isBlank()) {
            Toast.makeText(requireContext(), R.string.error_empty_title, Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        val workout = Workout(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            type = type,
            day = day,
            hour = hour,
            duration = duration
        )

        viewModel.addWorkout(workout)
        dialog.dismiss()
        Toast.makeText(requireContext(), R.string.workout_saved, Toast.LENGTH_SHORT).show()
    }

    cancelButton.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
} 