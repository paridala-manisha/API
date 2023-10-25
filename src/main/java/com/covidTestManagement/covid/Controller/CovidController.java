package com.covidTestManagement.covid.Controller;

import java.util.Date;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.covidTestManagement.covid.Service.EmailService;
import com.covidTestManagement.covid.model.Booking;
import com.covidTestManagement.covid.model.Patient;
import com.covidTestManagement.covid.model.Register;
import com.covidTestManagement.covid.repository.BookingRepository;
import com.covidTestManagement.covid.repository.PatientRepository;
import com.covidTestManagement.covid.repository.RegisterRepository;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class CovidController {
    @Autowired
    RegisterRepository registerrepository;
    
    @Autowired
    PatientRepository patientrepository;
    
    @Autowired
    BookingRepository bookingRepository;
    
    @Autowired
    private EmailService emailService;
    @Autowired
    private JavaMailSender emailSender;
    
    // Create a logger
    private static final Logger logger = Logger.getLogger(CovidController.class.getName());
    
    @PostMapping("/add")
    public String Registeradd(@RequestBody Register register) {
        try {
            // Save the user registration without storing the registration date
            registerrepository.save(register);
    
            // Send a registration confirmation email
            String to = register.getEmail();
            String subject = "[COVID-19 Test and Management] Registration Confirmation";
    
            StringBuilder body = new StringBuilder();
            body.append("Thank you for registering with COVID-19 Test and Management – your trusted resource for COVID-19 testing and results management. We are committed to ensuring your safety and well-being during these challenging times.\n\n");
            body.append("This email is to confirm your successful registration with COVID-19 Test and Management. Here are the details of your registration:\n\n");
            body.append("- Email Address: ").append(register.getEmail()).append("\n\n");
    
            // COVID-19 Testing Information
            body.append("COVID-19 Testing:\n");
            body.append("With COVID-19 Test and Management, you can:\n");
            body.append("1. Schedule COVID-19 tests easily.\n");
            body.append("2. View and manage your test results securely.\n");
            body.append("3. Receive important updates and guidelines related to COVID-19.\n\n");
    
            // Appointment Booking
            body.append("Appointment Booking:\n");
            body.append("If you have tested positive for COVID-19 and require medical assistance, you can book an appointment with a doctor through our platform.\n");
            body.append("Our team of healthcare professionals is here to support you during your recovery.\n\n");
    
            body.append("We take your privacy seriously, and your information will be handled securely in accordance with our privacy policy.\n\n");
            body.append("If you have any questions or require assistance, please don't hesitate to contact our support team at [paridalamanisha31@gmail.com] or [9390802867].\n\n");
            body.append("Thank you for choosing COVID-19 Test and Management as your COVID-19 management platform. Together, we can contribute to a safer and healthier community.\n\n");
            body.append("Stay safe and take care.\n\n");
            body.append("Sincerely,\n");
            body.append("The COVID-19 Test and Management Team\n");
    
            // Create a SimpleMailMessage and set its properties
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("Covid19 Test Management System <paridalamanisha31@gmail.com>");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body.toString());
    
            // Send the email
            emailService.sendEmail(message);
    
            // Log success
            logger.info("New member registered successfully.");
    
            return "New member is added";
        } catch (Exception e) {
            // Log error
            logger.severe("Error registering a new member: " + e.getMessage());
            return "An error occurred while registering a new member";
        }
    }
    
    @PutMapping("/updatePassword")
    public Map<String, String> updatePassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String newPassword = request.get("password");
    
            Optional<Register> userOptional = registerrepository.findByEmail(email);
    
            if (userOptional.isPresent()) {
                Register user = userOptional.get();
                user.setPassword(newPassword);
                registerrepository.save(user);
    
                // Password updated successfully
                logger.info("Password updated for user with email: " + email);
                return Map.of("success", "true");
            } else {
                // User not found with the given email
                logger.warning("User not found for password update with email: " + email);
                return Map.of("success", "false", "message", "User not found");
            }
        } catch (Exception e) {
            // Log error
            logger.severe("Error updating password: " + e.getMessage());
            return Map.of("success", "false", "message", "An error occurred while updating password");
        }
    }
    
    @PostMapping("/patientregister")
    public String patient(@RequestBody Patient patient) {
        try {
            patientrepository.save(patient);
            // Log success
            logger.info("New patient registered successfully.");
            return "New member is added";
        } catch (Exception e) {
            // Log error
            logger.severe("Error registering a new patient: " + e.getMessage());
            return "An error occurred while registering a new patient";
        }
    }
    
    @GetMapping("/getRegister")
    public List<Register> rlist() {
        try {
            List<Register> registers = registerrepository.findAll();
            // Log success
            logger.info("Retrieved list of registered users.");
            return registers;
        } catch (Exception e) {
            // Log error
            logger.severe("Error retrieving list of registered users: " + e.getMessage());
            return null;
        }
    }
    
    @GetMapping("/patients")
    public List<Patient> plist() {
        try {
            List<Patient> patients = patientrepository.findAll();
            // Log success
            logger.info("Retrieved list of patients.");
            return patients;
        } catch (Exception e) {
            // Log error
            logger.severe("Error retrieving list of patients: " + e.getMessage());
            return null;
        }
    }
    
    // delete patient by id
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Patient> deleteBook(@PathVariable("id") Integer id) {
        try {
            patientrepository.deleteById(id);
            // Log success
            logger.info("Patient with ID " + id + " deleted successfully.");
            return new ResponseEntity<Patient>(HttpStatus.OK);
        } catch (Exception e) {
            // Log error
            logger.severe("Error deleting patient with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<Patient>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // add new patient
    @PutMapping("/patient/Patients/{id}")
    public ResponseEntity<Patient> updatepatient(@PathVariable("id") Integer id, @RequestBody Patient patient) {
        try {
            Patient p = patientrepository.findById(id).get();
            if (p.getPatient_id() != 0) {
                p.setName(patient.getName());
                p.setEmail(patient.getEmail());
                p.setMobile(patient.getMobile());
                p.setDob(patient.getDob());
                p.setGovid(patient.getGovid());
                p.setAddress(patient.getAddress());
                p.setState(patient.getState());
            }
            // Log success
            logger.info("Patient with ID " + id + " updated successfully.");
            return new ResponseEntity<Patient>(patientrepository.save(p), HttpStatus.OK);
        } catch (Exception e) {
            // Log error
            logger.severe("Error updating patient with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<Patient>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/patient/Patients/{patient_id}")
    public ResponseEntity<Patient> updateresult(@PathVariable("patient_id") Integer patient_id, @RequestBody Patient patient) {
        try {
            Patient p = patientrepository.findById(patient_id).get();
            // Log success
            logger.info("Patient result updated successfully.");
            return new ResponseEntity<Patient>(patientrepository.save(p), HttpStatus.OK);
        } catch (Exception e) {
            // Log error
            logger.severe("Error updating patient result: " + e.getMessage());
            return new ResponseEntity<Patient>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/booking")
    public ResponseEntity<String> bookAppointment(@RequestBody Booking booking) {
        try {
            bookingRepository.save(booking);
    
            // Send a booking confirmation email
            String to = booking.getEmail();
            String subject = "Booking Confirmation";
            String body = "Dear " + booking.getPatientName() + ",\n\n"
                        + "Your appointment with Covid19 Test Management has been confirmed. Here are the details:\n\n"
                        + "Doctor's Name: " + booking.getDoctorName() + "\n"
                        + "Date: " + booking.getDate() + "\n"
                        + "Slot Time: " + booking.getSlot() + "\n"
                        + "Location: " + booking.getLocation() + "\n"
                        + "Profession: " + booking.getProfession() + "\n\n"
                        + "Thank you for choosing us!\n"
                        + "Covid19 Test Management Team";
    
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("Covid19 Test Management System <your-email@example.com>"); // Replace with your sender's email
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
    
            emailService.sendEmail(message);
    
            // Log success
            logger.info("Appointment booked successfully.");
    
            return new ResponseEntity<>("Appointment booked successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Log error
            logger.severe("Error booking appointment: " + e.getMessage());
            return new ResponseEntity<>("Failed to book appointment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/reset-password-request")
    public ResponseEntity<String> resetPassword(@RequestBody Register request) {
        try {
            // Check if the provided email address exists in your database
            // If it exists, send a password reset email

            String to = request.getEmail();
            String subject = "Password Reset Request";
            String body = "<html><body>"
                    + "<p>Dear User,</p>"
                    + "<p>You have recently asked to reset the password for this COVID-19 Test and Management account:</p>"
                    + "<p>" + request.getEmail() + "</p>"
                    + "<p>To update your password, click the link below:</p>"
                    + "<p><a href='http://localhost:3000/ResetPassword' style='color: blue; text-decoration: underline;'>Reset Password</a></p>"
                    + "<p>Regards, COVID-19 Test and Management</p>"
                    + "</body></html>";

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("Covid19 Test Management System <your-email@example.com>");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Set the content type to HTML

            // Send the email
            emailSender.send(message);

            // Log success
            logger.info("Password reset email sent successfully.");

            return new ResponseEntity<>("Password reset email sent successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Log error
            logger.severe("Failed to send password reset email: " + e.getMessage());
            return new ResponseEntity<>("Failed to send password reset email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/bookings") // Get all bookings
    public List<Booking> getAllBookings() {
        try {
            List<Booking> bookings = bookingRepository.findAll();
            // Log success
            logger.info("Retrieved list of bookings.");
            return bookings;
        } catch (Exception e) {
            // Log error
            logger.severe("Error retrieving list of bookings: " + e.getMessage());
            return null;
        }
    }
    
    @DeleteMapping("booking/{id}")
    public ResponseEntity<Booking> deleteBooking(@PathVariable("id") Integer id) {
        try {
            bookingRepository.deleteById(id);
            // Log success
            logger.info("Booking with ID " + id + " deleted successfully.");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // Log error
            logger.severe("Error deleting booking with ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/byEmail")
    public List<Booking> getBookingsByEmail(@RequestParam String email) {
        try {
            List<Booking> bookings = bookingRepository.findByEmail(email);
            // Log success
            logger.info("Retrieved bookings for email: " + email);
            return bookings;
        } catch (Exception e) {
            // Log error
            logger.severe("Error retrieving bookings for email: " + email + ", Error: " + e.getMessage());
            throw e; // You might want to handle exceptions more gracefully
        }
    }
}