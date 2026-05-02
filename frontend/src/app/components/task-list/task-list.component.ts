import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Task } from '../../models/task.model';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="min-h-screen bg-gradient-to-br from-indigo-900 via-purple-900 to-pink-900 text-white px-4 py-10 sm:px-6 lg:px-8">
      <div class="mx-auto flex w-full max-w-6xl flex-col gap-8">
        <header class="space-y-3 text-center sm:text-left">
          <p class="text-sm uppercase tracking-[0.35em] text-white/60">Task Manager</p>
          <h1 class="text-4xl font-semibold sm:text-5xl">Your tasks, floating in glass.</h1>
          <p class="max-w-2xl text-sm text-white/70 sm:text-base">
            Add tasks, mark them complete, and keep everything in sync with your Spring Boot API.
          </p>
        </header>

        <section class="backdrop-blur-md bg-white/10 border border-white/20 rounded-2xl shadow-lg p-5 sm:p-6">
          <h2 class="mb-4 text-xl font-semibold">Add a new task</h2>

          <form class="grid gap-4 md:grid-cols-2" #taskForm="ngForm" (ngSubmit)="addTask()">
            <label class="flex flex-col gap-2 md:col-span-1">
              <span class="text-sm text-white/80">Title</span>
              <input
                name="title"
                [(ngModel)]="newTask.title"
                required
                class="rounded-xl border border-white/20 bg-white/10 px-4 py-3 text-white placeholder-white/40 outline-none transition focus:border-white/40 focus:bg-white/15"
                placeholder="Write a task title"
              />
            </label>

            <label class="flex flex-col gap-2 md:col-span-1">
              <span class="text-sm text-white/80">Duration in minutes</span>
              <input
                name="durationMinutes"
                type="number"
                min="0"
                [(ngModel)]="newTask.durationMinutes"
                required
                class="rounded-xl border border-white/20 bg-white/10 px-4 py-3 text-white placeholder-white/40 outline-none transition focus:border-white/40 focus:bg-white/15"
                placeholder="30"
              />
            </label>

            <label class="flex flex-col gap-2 md:col-span-2">
              <span class="text-sm text-white/80">Description</span>
              <textarea
                name="description"
                [(ngModel)]="newTask.description"
                rows="4"
                class="rounded-xl border border-white/20 bg-white/10 px-4 py-3 text-white placeholder-white/40 outline-none transition focus:border-white/40 focus:bg-white/15"
                placeholder="Add more details about the task"
              ></textarea>
            </label>

            <div class="md:col-span-2 flex justify-end">
              <button
                type="submit"
                [disabled]="taskForm.invalid"
                class="rounded-xl bg-white px-5 py-3 font-medium text-slate-900 transition hover:scale-[1.02] hover:bg-white/90 disabled:cursor-not-allowed disabled:opacity-50"
              >
                Add Task
              </button>
            </div>
          </form>
        </section>

        <section class="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          <article
            *ngFor="let task of tasks"
            class="backdrop-blur-md bg-white/10 border border-white/20 rounded-2xl shadow-lg p-5 transition hover:-translate-y-1 hover:bg-white/15"
          >
            <div class="flex items-start justify-between gap-4">
              <h3
                class="text-xl font-semibold"
                [class.line-through]="task.completed"
                [class.text-white/50]="task.completed"
              >
                {{ task.title }}
              </h3>
              <span class="rounded-full border border-white/20 px-3 py-1 text-xs uppercase tracking-[0.2em] text-white/70">
                {{ task.durationMinutes }} min
              </span>
            </div>

            <p class="mt-3 text-sm leading-6 text-white/75" [class.line-through]="task.completed" [class.opacity-60]="task.completed">
              {{ task.description || 'No description provided.' }}
            </p>

            <div class="mt-5 flex items-center justify-between gap-3">
              <button
                type="button"
                (click)="toggleComplete(task.id)"
                class="rounded-xl border border-white/20 bg-white/10 px-4 py-2 text-sm font-medium text-white transition hover:bg-white/20"
              >
                {{ task.completed ? 'Mark Incomplete' : 'Mark Complete' }}
              </button>

              <button
                type="button"
                (click)="deleteTask(task.id)"
                class="rounded-xl border border-rose-300/30 bg-rose-400/10 px-4 py-2 text-sm font-medium text-rose-100 transition hover:bg-rose-400/20"
              >
                Delete
              </button>
            </div>
          </article>
        </section>

        <p *ngIf="tasks.length === 0" class="text-center text-sm text-white/60">
          No tasks yet. Add one above to get started.
        </p>
      </div>
    </div>
  `,
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  newTask: Partial<Task> = {
    title: '',
    description: '',
    durationMinutes: 0,
    completed: false,
  };

  constructor(private readonly taskService: TaskService) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getTasks().subscribe({
      next: (tasks) => {
        this.tasks = tasks;
      },
      error: (error) => {
        console.error('Failed to load tasks', error);
      },
    });
  }

  addTask(): void {
    if (!this.newTask.title?.trim()) {
      return;
    }

    const payload: Task = {
      id: '',
      title: this.newTask.title.trim(),
      description: this.newTask.description?.trim() ?? '',
      durationMinutes: Number(this.newTask.durationMinutes ?? 0),
      completed: false,
    };

    this.taskService.addTask(payload).subscribe({
      next: (createdTask) => {
        this.tasks = [createdTask, ...this.tasks];
        this.newTask = {
          title: '',
          description: '',
          durationMinutes: 0,
          completed: false,
        };
      },
      error: (error) => {
        console.error('Failed to add task', error);
      },
    });
  }

  deleteTask(id: string): void {
    this.taskService.deleteTask(id).subscribe({
      next: () => {
        this.tasks = this.tasks.filter((task) => task.id !== id);
      },
      error: (error) => {
        console.error('Failed to delete task', error);
      },
    });
  }

  toggleComplete(id: string): void {
    this.taskService.toggleComplete(id).subscribe({
      next: (updatedTask) => {
        this.tasks = this.tasks.map((task) => (task.id === updatedTask.id ? updatedTask : task));
      },
      error: (error) => {
        console.error('Failed to toggle task completion', error);
      },
    });
  }
}
