package fr.uge.tp4;

import fr.uge.tp4.models.Prescription;

public interface PrescriptionSender extends AutoCloseable {
    boolean sendPrescription(Prescription prescription, String topic);
    boolean sendPrescription(Prescription prescription, String topic, String key);
}
