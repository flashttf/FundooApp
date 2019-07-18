package com.bridgelabz.fundoo.user.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.user.dto.NoteDto;
import com.bridgelabz.fundoo.user.model.Label;
import com.bridgelabz.fundoo.user.model.Note;
import com.bridgelabz.fundoo.user.model.Response;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.INoteRepository;
import com.bridgelabz.fundoo.user.repository.IUserRepository;
import com.bridgelabz.fundoo.user.repository.ILabelRepository;
import com.bridgelabz.fundoo.utility.ITokenGenerator;
import com.bridgelabz.fundoo.utility.ResponseUtility;
import com.bridgelabz.fundoo.utility.Utility;

@Service
@PropertySource("classpath:message.properties")
public class NoteServiceImpl implements INoteService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Environment environment;

	@Autowired
	private IElasticSearchService elasticService;

	@Autowired
	private IUserRepository iUserRepository;

	@Autowired
	private INoteRepository iNoteRepository;

	@Autowired
	private ILabelRepository iLabelRepository;

	@Autowired
	private ITokenGenerator tokenGenerator;

	@Override
	public Response createNote(NoteDto noteDto, String token) {
		String id = tokenGenerator.verifyToken(token);
		Optional<User> isUser = iUserRepository.findByUserId(id);
		System.err.println(isUser.toString());
		if (isUser.isPresent()) {
			User user = iUserRepository.findById(id).get();
			Note note = modelMapper.map(noteDto, Note.class);
			note.setUserId(user.getUserId());
			note.setCreatedTime(Utility.currentDate());
			note.setUpdatedTime(Utility.currentDate());
			Note saveNote = iNoteRepository.save(note);

			List<Note> notesList = isUser.get().getNotes();
			if (notesList != null && !notesList.contains(note)) {
				notesList.add(note);
				user.setNotes(notesList);
				iUserRepository.save(user);
			} else {
				List<Note> newNotesList = new ArrayList<Note>();
				newNotesList.add(note);
				user.setNotes(newNotesList);
				iUserRepository.save(user);
			}

			try {
				elasticService.createNote(saveNote);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Response response = ResponseUtility.getResponse(200, "", environment.getProperty("note.create.success"));
			return response;
		} else {
			Response response = ResponseUtility.getResponse(204, "0", environment.getProperty("note.create.failed"));
			return response;
		}
	}

	@Override
	public Response updateNote(NoteDto noteDto, String token, String noteId) {
		String id = tokenGenerator.verifyToken(token);
		Optional<Note> isNote = iNoteRepository.findByNoteIdAndUserId(noteId, id);
		if (isNote.isPresent()) {
			isNote.get().setCreatedTime(Utility.currentDate());
			isNote.get().setTitle(noteDto.getTitle());
			isNote.get().setDescription(noteDto.getDescription());
			isNote.get().setUpdatedTime(Utility.currentDate());
			Note updatedNote = iNoteRepository.save(isNote.get());

			try {
				elasticService.updateNote(updatedNote);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Response response = ResponseUtility.getResponse(200, "", environment.getProperty("note.update.success"));
			return response;

		} else {
			Response response = ResponseUtility.getResponse(204, "0", environment.getProperty("note.update.failed"));
			return response;
		}

	}

	@Override
	public Response deleteNote(String token, String noteId) {
		String id = tokenGenerator.verifyToken(token);
		Optional<Note> isNote = iNoteRepository.findByNoteIdAndUserId(noteId, id);
		if (isNote.isPresent()) {

			Note note = iNoteRepository.findByNoteIdAndUserId(noteId, id).get();
			User user = iUserRepository.findById(id).get();
			List<Note> notes = user.getNotes();

			if (notes.stream().filter(u -> u.getNoteId().equals(note.getNoteId())).findFirst().isPresent()) {

				Note notes1 = notes.stream().filter(u -> u.getNoteId().equals(note.getNoteId())).findFirst().get();

				user.getNotes().remove(notes1);

				iNoteRepository.delete(notes1);
				iUserRepository.save(user);
			}

			try {
				elasticService.deleteNote(noteId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Response response = ResponseUtility.getResponse(200, "", environment.getProperty("note.delete.success"));
			return response;
		} else {
			Response response = ResponseUtility.getResponse(204, "0", environment.getProperty("note.delete.failed"));
			return response;
		}
	}

	@Override
	public List<Note> read(String token) {
		String userId = tokenGenerator.verifyToken(token);
		List<Note> notes = iNoteRepository.findByUserId(userId);
		List<Note> notesList = new ArrayList<>();
		for (Note userNotes : notes) {
			Note note = modelMapper.map(userNotes, Note.class);
			notesList.add(note);
		}
		return notesList;

	}

	@Override
	public Response setArchive(String token, String noteId) {
		String id = tokenGenerator.verifyToken(token);
		Optional<Note> note = iNoteRepository.findByNoteIdAndUserId(noteId, id);
		if (note.isPresent()) {
			note.get().setPin(false);
			if (note.get().isArchive() == false) {
				note.get().setArchive(true);
			} else {
				note.get().setArchive(false);
			}
			note.get().setUpdatedTime(Utility.currentDate());
			iNoteRepository.save(note.get());
			Response response = ResponseUtility.getResponse(200, "", environment.getProperty("note.archive.sucess"));
			return response;
		}
		Response response = ResponseUtility.getResponse(204, "0", environment.getProperty("note.archive.failed"));
		return response;
	}

	@Override
	public Response setTrash(String token, String noteId) {
		String id = tokenGenerator.verifyToken(token);
		Optional<Note> note = iNoteRepository.findByNoteIdAndUserId(noteId, id);
		if (note.isPresent()) {
			note.get().setTrash(true);
			note.get().setUpdatedTime(Utility.currentDate());
			iNoteRepository.save(note.get());
			Response response = ResponseUtility.getResponse(200, "", environment.getProperty("note.isTrash.sucess"));
			return response;
		}
		Response response = ResponseUtility.getResponse(201, "0", environment.getProperty("note.isTrash.failed"));
		return response;
	}

	@Override
	public Response setPin(String token, String noteId) {
		String id = tokenGenerator.verifyToken(token);
		Optional<Note> note = iNoteRepository.findByNoteIdAndUserId(noteId, id);
		if (note.isPresent()) {
			note.get().setArchive(false);
			;
			note.get().setTrash(true);
			note.get().setUpdatedTime(Utility.currentDate());
			iNoteRepository.save(note.get());
			Response response = ResponseUtility.getResponse(200, "", environment.getProperty("note.isPin.success"));
			return response;
		}
		Response response = ResponseUtility.getResponse(204, "0", environment.getProperty("note.isPin.failed"));
		return response;
	}

	@SuppressWarnings("unused")
	public Response addLabelToNote(String noteId, String token, String labelId) {
		String id = tokenGenerator.verifyToken(token);
		Optional<User> optionalUser = iUserRepository.findById(id);
		Optional<Note> optionalNote = iNoteRepository.findById(noteId);
		Optional<Label> optionalLabel = iLabelRepository.findById(labelId);
		if (optionalUser.isPresent() && optionalNote.isPresent() && optionalLabel.isPresent()) {
			Label label = optionalLabel.get();
			Note note = optionalNote.get();
			System.err.println(label);
			note.setUpdatedTime(Utility.currentDate());
			List<Label> labels = note.getLabels();
			System.out.println(labels);
			if (labels != null) {
				Optional<Label> newOPlabel = labels.stream().filter(l -> l.getLabelName().equals(label.getLabelName()))
						.findFirst();
				System.out.println(newOPlabel);
				if (!newOPlabel.isPresent()) {
					labels.add(label);
					note.setLabels(labels);
					note = iNoteRepository.save(note);
					System.out.println("Saved Label in note" + note);
					Response response = ResponseUtility.getResponse(200, "",
							environment.getProperty("label.toNote.added.success"));
					return response;
				}
			} else if (labels == null) {
				labels = new ArrayList<Label>();
				labels.add(label);
				note.setLabels(labels);
				iNoteRepository.save(note);
				Response response = ResponseUtility.getResponse(200, "",
						environment.getProperty("label.toNote.added.success"));
				return response;
			} else {
				Response response = ResponseUtility.getResponse(204, "",
						environment.getProperty("label.toNote.add.failed"));
				return response;
			}
		}
		Response response = ResponseUtility.getResponse(204, "0", environment.getProperty("label.toNote.add.failed"));
		return response;
	}

	@Override
	public Response removeLabelFromNote(String noteId, String token, String labelId) {
		String id = tokenGenerator.verifyToken(token);
		Optional<User> optionalUser = iUserRepository.findById(id);
		Optional<Note> optionalNote = iNoteRepository.findById(noteId);
		Optional<Label> optionalLabel = iLabelRepository.findById(labelId);
		if (!optionalUser.isPresent() && optionalNote.isPresent() && optionalLabel.isPresent()) {
			Response response = ResponseUtility.getResponse(204, "", environment.getProperty("note.not.found"));
			return response;
		} else {
			Label label = optionalLabel.get();
			Note note = optionalNote.get();
			note.setUpdatedTime(Utility.currentDate());
			List<Label> labelList = new ArrayList<Label>();
			labelList = note.getLabels();
			if (labelList.stream().filter(l -> l.getLabelId().equals(label.getLabelId())).findFirst().isPresent()) {
				Label findLabel = labelList.stream().filter(l -> l.getLabelId().equals(label.getLabelId())).findFirst()
						.get();
				labelList.remove(findLabel);
				iNoteRepository.save(note);
				Response response = ResponseUtility.getResponse(200, "",
						environment.getProperty("note.remove.label.success"));
				return response;
			}
			Response response = ResponseUtility.getResponse(204, "0",
					environment.getProperty("note.remove.label.fail"));
			return response;
		}
	}

	@Override
	public List<Label> getLabelOfNote(String noteId, String token) {
		String id=tokenGenerator.verifyToken(token);
		Optional<User> isUser=iUserRepository.findById(id);
		
		if(isUser.isPresent()) {
			Optional<Note> isNote=iNoteRepository.findById(noteId);
			return isNote.get().getLabels();
			
		}
		return null;
	}

	@Override
	public List<Note> getArchiveNote(String token) {
		String userId=tokenGenerator.verifyToken(token);
		Optional<User> isUser=iUserRepository.findById(userId);

		if(isUser.isPresent()) {
			return isUser.get().getNotes().stream().filter(l->l.isArchive()).collect(Collectors.toList());

		}
		return null;
	}

	

}
