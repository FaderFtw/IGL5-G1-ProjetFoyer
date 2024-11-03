package tn.esprit.tpfoyer17.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EtudiantServiceTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant result = etudiantService.addEtudiant(etudiant);

        assertEquals(etudiant, result);
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testGetAllEtudiants() {
        List<Etudiant> mockEtudiants = new ArrayList<>();
        when(etudiantRepository.findAll()).thenReturn(mockEtudiants);

        List<Etudiant> result = etudiantService.getAllEtudiants();

        assertEquals(mockEtudiants, result);
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void testGetEtudiantById_Found() {
        long idEtudiant = 1L;
        Etudiant mockEtudiant = new Etudiant();
        when(etudiantRepository.findById(idEtudiant)).thenReturn(Optional.of(mockEtudiant));

        Etudiant result = etudiantService.getEtudiantById(idEtudiant);

        assertEquals(mockEtudiant, result);
        verify(etudiantRepository, times(1)).findById(idEtudiant);
    }

    @Test
    void testGetEtudiantById_NotFound() {
        long idEtudiant = 1L;
        when(etudiantRepository.findById(idEtudiant)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () ->
                etudiantService.getEtudiantById(idEtudiant)
        );

        assertTrue(exception.getMessage().contains("Etudiant with ID " + idEtudiant + " not found"));
        verify(etudiantRepository, times(1)).findById(idEtudiant);
    }

    @Test
    void testDeleteEtudiant() {
        long idEtudiant = 1L;

        etudiantService.deleteEtudiant(idEtudiant);

        verify(etudiantRepository, times(1)).deleteById(idEtudiant);
    }

    @Test
    void testUpdateEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant result = etudiantService.updateEtudiant(etudiant);

        assertEquals(etudiant, result);
        verify(etudiantRepository, times(1)).save(etudiant);
    }
}
