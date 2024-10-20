$(document).ready(async function() {
    let userToken = await getTokenRequest(); // Get the user's token
    let userId = $('#userId').val();

    await loadProjects(userId, userToken); // Load projects
    $(document).on('click', '.filterSearch',(function(){ // Load projects by their id
        //-> If the clicked button has value for the category then load the      -> If the button has no value then call the
        //   list of projects filtered by their status                              the normal loadProjects function
        $(this).val() !== "" ? loadProjectsByCategory(userId, $(this).val(), userToken) : loadProjects(userId, userToken);
    }));
});

function loadProjects(id, userToken) {
    $.ajax({
        url: `http://localhost:8081/api/users/${id}/projects`,  // Server URL
        method: 'GET', // HTTP Method
        headers: {
            'Authorization': 'Bearer ' + userToken,
            'Content-Type': 'application/json'
        },
        success: function (projects) {
            const projectGrid = $('#scrollable-container'); // Project cards container
            projectGrid.empty(); // Clear previous projects

            // Initialize counters
            let totalProjects = 0;
            let deliveredProjects = 0;
            let inProgressProjects = 0;

            if (projects && projects.length > 0) {
                // Loop through each project and process it
                projects.forEach(project => {
                    // Increment total projects counter
                    totalProjects++;

                    // Set status color and update counters based on project status
                    let statusColor = '';
                    switch (project.status.name) {
                        case 'In Progress':
                            inProgressProjects++;
                            statusColor = 'bg-blue-600'; // In Progress
                            break;
                        case 'Completed':
                            deliveredProjects++;
                            statusColor = 'bg-green-500'; // Completed
                            break;
                        default:
                            statusColor = 'bg-gray-500'; // Default status
                            break;
                    }

                    // Create the project card with a click event
                    const projectCard = `
                    <div class="project-card bg-white p-5 rounded-lg border max-w-sm cursor-pointer" onclick="window.location.href='project/${project.id}'">
                        <div class="flex justify-between items-center">
                            <h3 class="text-xl font-bold text-gray-800 capitalize">${project.title}</h3>
                            <span class="text-white text-sm font-medium px-2.5 py-0.5 rounded-full capitalize ${statusColor}">${project.status.name}</span>
                        </div>
                        <p class="text-sm text-gray-500 mt-2 capitalize">Assigned to: ${project.user ? project.user.username : 'Not assigned'}</p>
                        <p class="text-sm text-gray-500 capitalize">Client: ${project.client.name}</p>
                        <p class="text-sm text-gray-500">Due Date: ${project.endDate}</p>
                    </div>
                `;

                    // Append the project card to the container
                    projectGrid.append(projectCard);
                });
            } else {
                projectGrid.append(`
                    <div class="flex items-center justify-center gap-3 text-center py-4 bg-light rounded" style="font-size: 1.2em; color: #6c757d;">
                        <i class="fas fa-times-circle" style="font-size: 2em; color: #dc3545;"></i>
                        <p class="mt-2">There are no projects available in this category yet...</p>
                    </div>
                `);
            }

            // Update counters on the interface
            $('#total-projects').text(totalProjects);
            $('#completed-projects').text(deliveredProjects);
            $('#in-progress-projects').text(inProgressProjects);

        },
        error: function (xhr, status, error) {
            console.error('Error loading projects:', error); // Error handling
        }
    });
}

function loadProjectsByCategory(id, projectCategory, userToken){
    $.ajax({
        url: `http://localhost:8081/api/users/${id}/projects`,  // Server URL
        method: 'GET', // HTTP Method
        headers: {
            'Authorization': 'Bearer ' + userToken,
            'Content-Type': 'application/json'
        },success: function (projects) {
            const projectGrid = $('#scrollable-container'); // Project cards container
            projectGrid.empty(); // Clear previous projects

            let hasProjects = false;
            projects.forEach((project) => { // Loop through each project and process it
                let statusColor = '';
                if(project.status.id == projectCategory){ // Comparing the status of the actual element with the searched category
                    switch (project.status.name) {
                        case 'In Progress': statusColor = 'bg-blue-600'; break;
                        case 'Completed': statusColor = 'bg-green-500'; break;
                    }

                    // Create the project card
                    const projectCard = `
                            <div class="project-card bg-white p-5 rounded-lg border max-w-sm cursor-pointer" onclick="window.location.href='project/${project.id}'">
                                <div class="flex justify-between items-center ">
                                    <h3 class="text-xl font-bold text-gray-800 capitalize">${project.title}</h3>
                                    <span class="text-white text-sm font-medium px-2.5 py-0.5 rounded-full capitalize ${statusColor}">${project.status.name}</span>
                                </div>
                                <p class="text-sm text-gray-500 mt-2 capitalize">Assigned to: ${project.user ? project.user.username : 'Not assigned'}</p>
                                <p class="text-sm text-gray-500 capitalize">Client: ${project.client.name}</p>
                                <p class="text-sm text-gray-500 capitalize">Due Date: ${project.endDate}</p>
                            </div>
                        `;

                    projectGrid.append(projectCard); // Append the project card to the container
                    hasProjects = true; // Set the flag in true to determine that there is at least a match
                }
            });
            if (!hasProjects) { // If there's no projects then...
                projectGrid.append(`
                        <div class="flex items-center justify-center gap-3 text-center py-4 bg-light rounded" style="font-size: 1.2em; color: #6c757d;">
                            <i class="fas fa-times-circle" style="font-size: 2em; color: #dc3545;"></i>
                            <p class="mt-2">There are no projects available in this category yet...</p>
                        </div>
                    `);
            }
        }
    });

}
