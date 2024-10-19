$(document).ready(async function() {
    let userToken = await getTokenRequest(); // Get the user's token

    await loadProjects(userToken); // Load projects
});

function loadProjects(userToken) {
    $.ajax({
        url: 'http://localhost:8081/api/projects',  // Server URL
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

                // Create the project card
                const projectCard = `
                    <div class="project-card bg-white p-5 rounded-lg border max-w-sm cursor-pointer" onclick="window.location.href='project/${project.id}'">
                        <div class="flex justify-between items-center">
                            <h3 class="text-xl font-bold text-gray-800">${project.title}</h3>
                            <span class="text-white text-sm font-medium px-2.5 py-0.5 rounded-full ${statusColor}">${project.status.name}</span>
                        </div>
                        <p class="text-sm text-gray-500 mt-2">Assigned to: ${project.user ? project.user.username : 'Not assigned'}</p>
                        <p class="text-sm text-gray-500">Customer: ${project.client.name}</p>
                        <p class="text-sm text-gray-500">Due Date: ${project.dueDate}</p>
                    </div>
                `;

                // Append the project card to the container
                projectGrid.append(projectCard);
            });

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
