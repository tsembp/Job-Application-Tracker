const form = document.getElementById('jobForm');
const applicationsList = document.getElementById('applicationsList');

// Add application
form.onsubmit = async(event) => {
    event.preventDefault();
    const company = document.getElementById('company').value;
    const position = document.getElementById('position').value;
    const location = document.getElementById('location').value;
    const date = document.getElementById('applicationDate').value;
    const status = document.getElementById('status').value;
    const jobType = document.getElementById('jobType').value;
    const notes = document.getElementById('notes').value;

    // Example JSON:
    // {
    //     "company": "Amazon",
    //     "position": "AI Software Engineer",
    //     "status": "REJECTED",
    //     "applicationDate": "02-01-2025",
    //     "notes": "Looking forward to receiving a reply.",
    //     "jobType" : "HYBRID",
    //     "location" : "Amsterdam, Netherlands"
    // }

    const jobApplication = {
        company: company,
        position: position,
        status: status,
        date: date,
        notes: notes,
        jobType: jobType,
        location: location
    }

    // POST request to backend
    const response = await fetch("http://localhost:8080/api/jobapplications", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(jobApplication)
    });

    if(response.ok){
        console.log('Application added successfully');
        fetchApplications();
    } else{
        console.log('Error adding application');
    }
}

// Get application
async function fetchApplications() {
    const response = await fetch('http://localhost:8080/api/jobapplications');
    const data = await response.json; // get applications in json format

    applicationsList.innerHTML = ''; // clear list
    data.forEach(application =>{
        const row = document.createElement('tr'); // create list for applications
        row.innerHTML = `
            <td>${app.company}</td>
            <td>${app.position}</td>
            <td>${app.status}</td>
            <td>${app.jobType}</td>
            <td>${app.location}</td>
            <td>${app.applicationDate}</td>
            <td>${app.notes}</td>
        `;
        applicationsList.appendChild(row);
    });
}

fetchApplications();