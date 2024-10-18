document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('new-project-form');
    const grid = document.getElementById('project-grid');
    const toggleButton = document.getElementById('toggle-form-button');
    const scrollableContainer = document.getElementById('scrollable-container');

    toggleButton?.addEventListener('click', () => {
        // Toggles Form Visibility
        form.classList.toggle('hidden');

        // Toggles between grid-cols-1 y grid-cols-2
        if (form.classList.contains('hidden')) {
            grid.classList.add('lg:grid-cols-1');
            grid.classList.remove('lg:grid-cols-2');
            scrollableContainer.classList.add('2xl:grid-cols-3');

            toggleButton.textContent = 'Add New Project +'; // Changes button's text to "Add New Project +"
        } else {
            grid.classList.add('lg:grid-cols-2');
            grid.classList.remove('lg:grid-cols-1');
            scrollableContainer.classList.remove('2xl:grid-cols-3');
            toggleButton.textContent = 'Cancel'; // Changes button's text to "Cancel"
        }
    });
});