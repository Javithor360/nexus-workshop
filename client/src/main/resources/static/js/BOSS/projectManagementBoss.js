$(document).ready(async function() {
    let userToken = await getTokenRequest(); // Get the user's token

    await loadClients(userToken); // Load clients for the dropdown
    await loadProjects(userToken); // Load existing projects

    // Initialize form validation
    $("#create-project-form").validate({
        rules: {
            projectName: {
                required: true,
                maxlength: 30 // Limit project name to 30 characters
            },
            description: {
                required: true,
                maxlength: 255 // Limit description to 255 characters
            },
            startDate: {
                required: true,
                date: true,
                min: new Date().toISOString().split("T")[0] // Start date must be today or in the future
            },
            dueDate: {
                required: true,
                date: true,
                greaterThan: "#start-date" // Due date must be after start date
            }
        },
        messages: {
            projectName: {
                required: "Please enter a project name",
                maxlength: "Project name cannot exceed 30 characters"
            },
            description: {
                required: "Please enter a description",
                maxlength: "Description cannot exceed 255 characters"
            },
            startDate: {
                required: "Please select a start date",
                date: "Please enter a valid date",
                min: "Start date must be today or later"
            },
            dueDate: {
                required: "Please select a due date",
                date: "Please enter a valid date",
                greaterThan: "Due date must be after the start date"
            }
        },
        submitHandler: function(form) {
            // Submit the form using AJAX
            createProject(userToken, form);
        }
    });
});

// Custom validator for due date
$.validator.addMethod("greaterThan", function(value, element, param) {
    return this.optional(element) || new Date(value) > new Date($(param).val());
}, "Due date must be after the start date");

async function loadClients(userToken) {
    $.ajax({
        url: 'http://localhost:8081/api/clients',  // Server URL to get clients
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + userToken,
            'Content-Type': 'application/json'
        },
        success: function(clients) {
            const customerSelect = $('#customer'); // Select the client dropdown element
            customerSelect.empty(); // Clear previous options
            customerSelect.append('<option value="">Select a Client</option>'); // Default option

            // Populate the select element with client names
            clients.forEach(client => {
                customerSelect.append(`<option value="${client.id}">${client.name}</option>`);
            });
        },
        error: function(xhr, status, error) {
            console.error('Error loading clients:', error); // Error handling
        }
    });
}

function createProject(userToken, form) {
    const projectData = {
        title: $(form).find('input[name="projectName"]').val(),
        description: $(form).find('textarea[name="description"]').val(),
        client_id: $(form).find('select[name="customer"]').val(),
        user_id: $(form).find('select[name="userId"]').val(),
        status_id: $(form).find('select[name="statusId"]').val(),
        start_date: $(form).find('input[name="startDate"]').val(),
        due_date: $(form).find('input[name="dueDate"]').val()
    };

    console.log('Project Data:', projectData);

    $.ajax({
        url: 'http://localhost:8081/api/projects',  // Server URL to create a new project
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + userToken,
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(projectData),
        success: function(response) {
            console.log('Project created:', response); // Success handling
            $(form)[0].reset(); // Reset the form after successful submission
            loadProjects(userToken); // Reload projects to reflect the new addition
        },
        error: function(xhr, status, error) {
            console.error('Error creating project:', error); // Error handling
        }
    });
}

function loadProjects(userToken) {
    $.ajax({
        url: 'http://localhost:8081/api/projects',  // Server URL to get projects
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
                        <p class="text-sm text-gray-500">Client: ${project.client.name}</p>
                        <p class="text-sm text-gray-500">Due Date: ${project.due_date}</p>
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
