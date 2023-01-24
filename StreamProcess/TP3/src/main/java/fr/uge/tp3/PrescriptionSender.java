package fr.uge.tp3;

import fr.uge.tp3.models.Prescription;

public interface PrescriptionSender extends AutoCloseable {
    boolean sendPrescription(Prescription prescription, String topic);
}
